package com.callsintegration.reader;

import org.springframework.batch.item.ItemReader;

/**
 * Created by berz on 07.03.2016.
 */
public interface ResetableItemReader<T> extends ItemReader {
    /*
    *   Функция, сбрасывающая reader в исходные условия
     */
    public void reset();
}
