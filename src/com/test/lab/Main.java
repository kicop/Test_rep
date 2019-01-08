package com.test.lab;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws Exception {
        List<Service> list = new ArrayList<>();
        Pattern pattern = Pattern.compile("(.*) (\\d{2}:\\d{2}) (\\d{2}:\\d{2})");
        Scanner in = new Scanner(new FileReader("D:/input.txt"));
        while (in.hasNextLine()){
            Matcher m = pattern.matcher(in.nextLine());
            while (m.find()) {
                list.add(new Service(m.group(1),LocalTime.parse(m.group(2)),LocalTime.parse(m.group(3))));
            }
        }
        in.close();
        cutter(list);
        list.sort(Comparator.comparing(Service::getDeparture));
        Comparator<Service> reverseComparator = Comparator.comparing(Service::getName);
        list.sort(reverseComparator.reversed());
        FileWriter writer = new FileWriter(new File("D:/output.txt"));
        boolean _if = true;
        for (Service n : list) {
            if (_if && n.getName().equals("Grotty")) {
                writer.write(System.lineSeparator() + n + System.lineSeparator());
                _if = false;
            } else
                writer.write(n + System.lineSeparator());
        }

        writer.close();
    }

    private static void cutter(List<Service> list){
        LocalTime t1i;
        LocalTime t1j;
        LocalTime t2i;
        LocalTime t2j;
        for (int i = list.size() - 1; i>=0; i--) {
            for (int j = list.size() - 1; j>=0; j--) {
                Duration duration = Duration.between(list.get(i).getDeparture(),list.get(i).getArrival());
                t1i = list.get(i).getDeparture();
                t1j = list.get(j).getDeparture();
                t2i = list.get(i).getArrival();
                t2j = list.get(j).getArrival();
                if (t1i.equals(t1j) && t2i.compareTo(t2j) > 0 || t2i.equals(t2j) && t1i.compareTo(t1j) < 0 ||
                    t1i.compareTo(t1j) < 0 && t2i.compareTo(t2j) > 0 ||
                    t1i.equals(t1j) && t2i.equals(t2j) && list.get(i).getName().equals("Grotty") && !(list.get(i).getName().equals(list.get(j).getName())) ||
                    duration.toHours() > 1) {
                    list.remove(i);
                }
            }
        }
    }

}

class Service {

    private String name;
    private LocalTime departure;
    private LocalTime arrival;

    Service(String name, LocalTime departure, LocalTime arrival){
        this.name = name;
        this.departure = departure;
        this.arrival = arrival;
    }

    @Override
    public String toString() {
        return name+" "+departure+" "+arrival;
    }

    String getName() {
        return name;
    }

    LocalTime getDeparture() {
        return departure;
    }

    LocalTime getArrival() {
        return arrival;
    }

}
