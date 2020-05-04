package com.twitter.anubhav.parsers;

public interface Parser<T, K> {
    K parse(T input);
}
