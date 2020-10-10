package com.github.systeminvecklare.cml.util;

import com.github.systeminvecklare.cml.ICmlNode;

public interface IConverter<T> {
	T convert(ICmlNode cmlNode);
}
