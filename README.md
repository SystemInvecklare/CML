# CML

#### Q: What is CML?
##### A: This is CML:
```
tag {
	childTag
	childTag {
		subTagToChild(id="test",example="yes")
		other(more="data") {
			child
			child child
		}
		funny = joke
		mummy = natalie(niceness="10")
		daddy = hatman(name="Matte", age="NonOfYourDamnBusiness") {
			humour = funnyHumour
			randomChild
		}
	}
}
```

#### Q: What does CML stand for?
##### A: "Compact Markup Language"

#### Q: Do we need yet annother markup launguage?
##### A: Yes.

#### Q: Isn't it enough with XML, JSON, YAML and all the rest of them?
##### A: No.

#### Q: What is good about CML?
##### A: Here is a list:
* Open source parser
* More compact than XML
* More straightforward than Json
* Not dependent on indentation nor spaces
* Simple
* Expressive


## Introduction
A CML document consists of a *CML node* which is the root node. The smallest possible CML document consists of just
a root node. Example:
```
thisIsMyNode
```
That is a valid CML document containing a root node with the name "thisIsMyNode".

Nodes can also have children, or *child nodes*. The child nodes of a node is contained within curly brackets.
```
thisIsMyNode {
	thisIsMyChildNode {
	}
	thisIsMyOtherChildNode {
	}
	thisIsAChildNodeWithAChildNodeOfItsOwn {
		hello
	}
}
```
The contents of the curly brackets after a node name defines the *body* of the node. Omitting the curly brackets 
is equivalent to defining an empty body.
```
node otherNode {child}
```
is equivalent to
```
node {


} otherNode {child}
```

A node can have attributes. Attributes are key-value-pairs of names and strings. These are defined within round brackets directly after the name of the node (not counting whitespace characters).
```
node(attribute1="value1",attribute2="value2") {}
```

If a node only have one single attribute with the name `value`, the name can be omitted:
```
container("text")
```
In the above example we can see a node named 'container' which has a single attribute by the name 'value' (implicit) 
with the value "text".

A node can not have two attributes with the same name.

A node can also have properties. Properties are key-value-pairs of names and nodes.
```
exampleNode {
	prop = imANode(attr="ex") {
		childNode
	}
}
```
In the above example we see a node named 'exampleNode' which has a property named 'prop' with a value which is a 
node with the name 'imANode'. 'imANode' has an attribute named 'attr' with the value "ex" and a child node named 'childNode'.

Just like attribute-names, property-names must be unique. So a node can not have two properties with the same name.

Let's look at a more complex CML document.
```
configuration {
	ip = localIp("192.168.0.1")
	servlets {
		servlet(type="Wildfly") {
			ports = singlePort("80")
			load {
				war(location="../webServer.war") {
					routing = allOf {
						pathBeginsWith("/server")
						not {
							pathBeginsWith("/server/server")
						}
					}
				}
				war(location="../adminInterface.war") {
					routing = anyOf {
						pathBeginsWith("/admin")
						pathBeginsWith("/server/admin")
					}
				}
			}
		}
		servlet(type="Tomcat") {
			ports = defaultHttps
			load {
				directory(location="../tomcat/")
			}
		}
	}
}
```