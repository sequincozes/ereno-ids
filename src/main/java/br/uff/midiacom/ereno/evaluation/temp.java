/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.midiacom.ereno.evaluation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author silvio
 */
public class temp {

    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();
        Date dateToday = cal.getTime();
        SimpleDateFormat WeekDayFor = new SimpleDateFormat("E");
        String weekday = WeekDayFor.format(dateToday);
        System.out.println("Week: " + weekday);
    }
}
