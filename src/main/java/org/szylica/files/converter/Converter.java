package org.szylica.data.converter;

public interface Converter<T, U> {

    U convert(T t);

}
