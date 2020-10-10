package com.github.systeminvecklare.cml.parser;

import java.io.IOException;
import java.io.Reader;

import com.github.systeminvecklare.cml.ICmlNode;

public final class CmlParser implements ICmlParser {

	@Override
	public void parse(Reader reader, ICmlParseHandler parseHandler) throws IOException {
		parse(new ReaderCharSource(reader), parseHandler);
	}

	@Override
	public ICmlNode parse(Reader reader) throws IOException {
		return parse(new ReaderCharSource(reader));
	}
	
	@Override
	public ICmlNode parse(ICharSource charSource) throws IOException {
		CmlNodeBuilder nodeBuilder = new CmlNodeBuilder();
		parse(charSource, nodeBuilder);
		return nodeBuilder.getRootNode();
	}
	
	@Override
	public void parse(ICharSource charSource, ICmlParseHandler parseHandler) throws IOException {
		new Parsing(charSource, parseHandler);
	}

	private static class Parsing {
		private final ICmlParseHandler parseHandler;
		private IState state = new ParseNodeHead(new Finished());

		public Parsing(ICharSource charSource, ICmlParseHandler parseHandler) throws IOException {
			this.parseHandler = parseHandler;
			new Tokenization(charSource, new ITokenHandler() {
				@Override
				public void onToken(IToken token) {
					try {
						state.feedToken(token);
					} catch(RuntimeException e) {
						throw new RuntimeException("Exception at "+token.startPos()+" to "+token.endPos()+": "+e.getMessage(), e);
					}
				}
			});
		}
		
		private void setState(IState newState) {
			this.state = newState;
		}
		
		private boolean isSyntax(IToken token, char syntax) {
			return token instanceof SyntaxToken && ((SyntaxToken) token).getSyntax() == syntax;
		}
		
		private class Finished implements IState {

			@Override
			public void feedToken(IToken token) {
				throw new RuntimeException("Unexpected token after rootNode: "+token);
			}
		}
		
		private class ParseNodeHead implements IState {
			private final IState parentState;
			private int step = 0;
			
			public ParseNodeHead(IState parentState) {
				this.parentState = parentState;
			}

			@Override
			public void feedToken(IToken token) {
				if(step == 0) {
					step = 1;
					if(token instanceof NameToken) {
						String name = ((NameToken) token).getText();
						parseHandler.onTagName(name);
					} else {
						throw new RuntimeException("Unexpected "+token);
					}
				} else if(step == 1) {
					step = 2;
					if(isSyntax(token, '(')) {
						parseHandler.onTagAttributesStart();
						setState(new ParseAttributes(this));
					} else {
						feedToken(token);
					}
				} else if(step == 2) {
					step = 3;
					if(isSyntax(token, '{')) {
						parseHandler.onTagBodyStart();
						setState(new ParseNodeBody(this));
					} else {
						feedToken(token);
					}
				} else {
					setState(parentState);
					state.feedToken(token);
				}
			}
		}
		
		private class ParseAttributes implements IState {
			private final IState parentState;
			private int step = 0;
			private String lastName = null;
			private boolean mightBeShort = true;
			
			public ParseAttributes(IState parentState) {
				this.parentState = parentState;
			}

			@Override
			public void feedToken(IToken token) {
				if((step == 0 || step == 3) && token instanceof SyntaxToken && ((SyntaxToken) token).getSyntax() == ')') {
					parseHandler.onTagAttributesEnd();
					setState(parentState);
				} else {
					if(step == 0) {
						step = 1;
						if(token instanceof NameToken) {
							lastName = ((NameToken) token).getText();
							mightBeShort = false;
						} else if(mightBeShort && token instanceof StringToken) {
							parseHandler.onTagAttribute("value", ((StringToken) token).getText());
							step = 4;
						} else {
							throw new RuntimeException("Expected name");
						}
					} else if(step == 1) {
						if(isSyntax(token, '=')) {
							step = 2;
						} else {
							throw new RuntimeException("Expected '='");
						}
					} else if(step == 2) {
						if(token instanceof StringToken) {
							parseHandler.onTagAttribute(lastName, ((StringToken) token).getText());
							lastName = null;
							step = 3;
						} else {
							throw new RuntimeException("Expected string (ex: \"value\")");
						}
					} else if(step == 3) {
						if(isSyntax(token, ',')) {
							step = 0;
						} else {
							throw new RuntimeException("Expected ',' or ')'");
						}
					} else if(step == 4) {
						if(isSyntax(token, ')')) {
							parseHandler.onTagAttributesEnd();
							setState(parentState);
						} else {
							throw new RuntimeException("Expected ')'");
						}
					} else {
						throw new IllegalStateException();
					}
				}
			}
		}
		
		private class ParseNodeBody implements IState {
			private final IState parentState;
			private NameToken lastName = null;
			
			public ParseNodeBody(IState parentState) {
				this.parentState = parentState;
			}

			@Override
			public void feedToken(IToken token) {
				if(isSyntax(token, '}')) {
					feedChildMaybe();
					parseHandler.onTagBodyEnd();
					setState(parentState);
				} else {
					if(lastName == null) {
						if(token instanceof NameToken) {
							lastName = (NameToken) token;
						} else {
							throw new RuntimeException("Expected name");
						}
					} else {
						NameToken name = lastName;
						lastName = null;
						if(isSyntax(token, '=')) {
							parseHandler.onProperty(name.getText());
							setState(new ParseNodeHead(this));
						} else {
							setState(new ParseNodeHead(this));
							state.feedToken(name);
							state.feedToken(token);
						}
					}
				}
			}

			private void feedChildMaybe() {
				if(lastName != null) {
					parseHandler.onTagName(lastName.getText());
					lastName = null;
				}
			}
			
		}
	}
	
	private static class Tokenization {
		private final ITokenHandler tokenHandler;
		private StringBuilder stringBuilder = new StringBuilder();
		private TokenFactory tokenFactory = new TokenFactory();

		public Tokenization(ICharSource charSource, ITokenHandler tokenHandler) throws IOException {
			this.tokenHandler = tokenHandler;
			int c = -1;
			while((c = charSource.read()) != -1) {
				tokenFactory.update((char) c);
				if(Character.isWhitespace(c)) {
					endToken();
				} else if(isSyntax((char) c)) {
					endToken();
					tokenFactory.markStart();
					tokenFactory.markEnd();
					tokenHandler.onToken(tokenFactory.syntax((char) c));
				} else if(c == '"') {
					endToken();
					tokenFactory.markStart();
					String string = parseString(charSource);
					tokenFactory.markEnd();
					tokenHandler.onToken(tokenFactory.string(string));
				} else {
					if(stringBuilder.length() == 0) {
						tokenFactory.markStart();
					}
					tokenFactory.markEnd();
					stringBuilder.append((char) c);
				}
			}
			endToken();
		}

		private String parseString(ICharSource charSource) throws IOException {
			StringBuilder str = new StringBuilder();
			boolean escaped = false;
			int c = -1;
			while((c = charSource.read()) != -1) {
				tokenFactory.update((char) c);
				if(escaped) {
					escaped = false;
					str.append((char) c);
				} else {
					if(c == '"') {
						return str.toString();
					} else if(c == '\\') {
						escaped = true;
					} else {
						str.append((char) c);
					}
				}
			}
			throw new RuntimeException("Expected '\"'");
		}

		private boolean isSyntax(char c) {
			return c == '{' || c == '}' || c == ',' || c == '(' || c == ')' || c == '=';
		}

		private void endToken() {
			if(stringBuilder.length() > 0) {
				String token = stringBuilder.toString();
				stringBuilder.setLength(0);
				tokenHandler.onToken(tokenFactory.name(token));
			}
		}
		
	}
	
	private interface IState {
		void feedToken(IToken token);
	}

	private interface ITokenHandler {
		void onToken(IToken token);
	}
	
	private interface ITokenFactory {
		void markStart();
		void markEnd();
		IToken syntax(char syntax);
		IToken name(String name);
		IToken string(String text);
	}
	
	private static class TokenFactory implements ITokenFactory {
		private int column = 0;
		private int row = 0;
		private ISourcePosition start;
		private ISourcePosition end;
		
		public void update(char c) {
			if(c == '\n') {
				column = 0;
				row++;
			} else if(c == '\t') {
				column += 4; //TODO make configurable
			} else {
				column++;
			}
		}
		
		@Override
		public IToken syntax(char syntax) {
			return new SyntaxToken(start, end, syntax);
		}

		@Override
		public IToken name(String name) {
			return new NameToken(start, end, name);
		}

		@Override
		public IToken string(String text) {
			return new StringToken(start, end, text);
		}

		@Override
		public void markStart() {
			start = new SourcePosition(column, row);
		}

		@Override
		public void markEnd() {
			end = new SourcePosition(column, row);
		}
	}
}
