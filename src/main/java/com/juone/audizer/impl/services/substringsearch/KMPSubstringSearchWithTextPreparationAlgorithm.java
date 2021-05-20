package com.juone.audizer.impl.services.substringsearch;


import javax.annotation.Nonnull;

public class KMPSubstringSearchWithTextPreparationAlgorithm extends AbstractSubstringSearchWithTextPreparationAlgorithm {
    private static final int R = 1256;

    @Override
    protected int searchImpl(@Nonnull String pattern) {
        int[] possibleResults = getPossibleResults(pattern.charAt(0));
        if (possibleResults.length == 0) {
            return -1;
        }
        int[][] dfa;
        int m = pattern.length();
        dfa = new int[R][m];
        dfa[pattern.charAt(0)][0] = 1;
        for (int x = 0, j = 1; j < m; j++) {
            for (int c = 0; c < R; c++) {
                dfa[c][j] = dfa[c][x];
            }
            dfa[pattern.charAt(j)][j] = j + 1;
            x = dfa[pattern.charAt(j)][x];
        }

        int n = text.length();
        int i, j, k;
        for (k = 0, j = 0, i = possibleResults[k]; i < n && j < m;) {
            j = dfa[text.charAt(i)][j];
            if (j == 0 && k < possibleResults.length - 1) {
                do {
                    k++;
                }   while (i > possibleResults[k]);
                i = possibleResults[k];
            } else {
                i++;
            }
        }
        if (j == m) {
            return i - m;
        }
        return -1;
    }
}