package com.ds.Helpers;

import java.time.LocalDate;
import java.util.Date;

public class ValidationHelper {

    public boolean isTCValid(String tc) {
        if (tc == null) return false;

//      1 - Uzunluk 11 karakter olmalı
        if (tc.length() != 11) {
            System.out.println("uzunluk");
            return false;
        }

//      2 - Her hanesi bir rakam olmalıdır.
        if (!tc.matches("[0-9]*")) {
            System.out.println("rakam");
            return false;
        }

//      3 - İlk hanesi 0 olamaz.
        if (Character.getNumericValue(tc.charAt(0)) == 0) {
            System.out.println("ilk hane 0");
            return false;
        }

//      4 - 1,3,5,7,9 basamaklar toplamının 7 katından 2,4,6,8 basamaklar toplamı çıkarılr ve bunun 10 ile modu alınırsa kalan sayı 10.basamaktaki sayıyı vermelidir.
        var tekBasTop = 0;
        var ciftBasTop = 0;

        for (var i = 0; i < 9; i++) {
            if (i % 2 == 0) {
                tekBasTop += Character.getNumericValue(tc.charAt(i));
            } else {
                ciftBasTop += Character.getNumericValue(tc.charAt(i));
            }
        }

        int result = (7 * tekBasTop) - ciftBasTop;
        result = result % 10;
        if (result != Character.getNumericValue(tc.charAt(9))) {
            System.out.println("tekler çiftler şartı");
            return false;
        }

//      5 - İlk 10 hanenin toplamından elde edilen sonucun 10 ile modu alınırsa 11. basamaktaki sayı elde edilmedilir.
        int basTop = 0;
        for (int i = 0; i < 10; i++) {
            basTop += Character.getNumericValue(tc.charAt(i));
        }

        if (basTop % 10 != Character.getNumericValue(tc.charAt(10))) {
            System.out.println("11.basamak şartı");
            return false;
        }

        return true;
    }

    public boolean isDateBetween(LocalDate date, LocalDate start, LocalDate end) {
        if (date == null || start == null || end == null) return false;

        return !date.isBefore(start) && !date.isAfter(end);
    }
}
