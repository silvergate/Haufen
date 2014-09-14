package com.dcrux.haufen.kugel.check;

import com.dcrux.haufen.kugel.ICheck;

/**
 * Created by caelis on 14/09/14.
 */
public interface ICodePointCheck extends ICheck {
    ICodePointCheck isEqual(int numberOfCodePoints);

    ICodePointCheck isGt(int numberOfCodePoints);

    ICodePointCheck isLe(int numberOfCodePoints);
}
