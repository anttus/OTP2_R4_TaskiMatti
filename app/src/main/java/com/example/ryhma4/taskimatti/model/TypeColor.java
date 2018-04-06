package com.example.ryhma4.taskimatti.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by anttu on 29.3.2018.
 */

public class TypeColor {

//    Red #F44336
//    Pink #E91E63
//    Purple #9C27B0
//    Deep Purple #673AB7
//    Indigo #3F51B5
//    Blue #2196F3
//    Light Blue #03A9F4
//    Cyan #00BCD4
//    Teal #009688
//    Green #4CAF50
//    Light Green #8BC34A
//    Lime #CDDC39
//    Yellow #FFEB3B
//    Amber #FFC107
//    Orange #FF9800
//    Deep Orange #FF5722
//    Brown #795548
//    Grey #9E9E9E
//    Blue Grey #607D8B

    private static List<String> colors = new ArrayList<>();

    private static final Random r = new Random();

    private static List<String> getColors() {
        return colors;
    }

    private static void addColors() {
        colors.add("#F44336");
        colors.add("#E91E63");
        colors.add("#9C27B0");
        colors.add("#673AB7");
        colors.add("#3F51B5");
        colors.add("#2196F3");
        colors.add("#03A9F4");
        colors.add("#00BCD4");
        colors.add("#009688");
        colors.add("#4CAF50");
        colors.add("#8BC34A");
        colors.add("#CDDC39");
        colors.add("#FFEB3B");
        colors.add("#FFC107");
        colors.add("#FF9800");
        colors.add("#FF5722");
        colors.add("#795548");
        colors.add("#9E9E9E");
        colors.add("#607D8B");
    }

    public static String randomColor() {
        addColors();
        //minimum + rn.nextInt(maxValue - minvalue + 1)
        int size = getColors().size();
        String randomColor;
        int random = 1 + r.nextInt(size);
        randomColor = getColors().get(random);
        return randomColor;
    }
}
