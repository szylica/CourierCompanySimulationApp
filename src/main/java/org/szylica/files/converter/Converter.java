package org.szylica.files.converter;

public interface Converter<T, U> {

    U convert(T t);

}
