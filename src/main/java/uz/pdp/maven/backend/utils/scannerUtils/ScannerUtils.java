package uz.pdp.maven.backend.utils.scannerUtils;

import java.util.Scanner;

public interface ScannerUtils {
    Scanner scanNum = new Scanner(System.in);
    Scanner scanStr = new Scanner(System.in);

    static int getInt(String hint){
        System.out.print(hint);
        return scanNum.nextInt();
    }

    static String getStr(String hint){
        System.out.print(hint);
        return scanStr.nextLine();
    }
}
