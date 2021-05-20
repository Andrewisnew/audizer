package com.juone.audizer.impl.services.substringsearch;


import javax.annotation.Nonnull;

import static java.util.Objects.requireNonNull;

public abstract class AbstractSubstringSearchAlgorithm {
    protected String text = "";

    public void setText(@Nonnull String text) {
        this.text = requireNonNull(text, "text");
    }

    public int search(@Nonnull String pattern) {
        requireNonNull(pattern, "pattern");
        return searchImpl(pattern);
    }

    protected abstract int searchImpl(@Nonnull String pattern);
}
