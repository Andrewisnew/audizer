package com.juone.audizer.impl.services.substringsearch;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import javax.annotation.Nonnull;

public abstract class AbstractSubstringSearchWithTextPreparationAlgorithm extends AbstractSubstringSearchAlgorithm {
    private final Multimap<Character, Integer> preparedTextData = ArrayListMultimap.create();

    @Override
    public void setText(@Nonnull String text) {
        super.setText(text);
        preparedTextData.clear();
        for (int i = 0; i < text.length(); i++) {
            preparedTextData.put(text.charAt(i), i);
        }
    }

    protected int[] getPossibleResults(char character) {
        return preparedTextData.get(character).stream().mapToInt(Integer::intValue).toArray();
    }
}
