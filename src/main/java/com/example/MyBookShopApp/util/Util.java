package com.example.MyBookShopApp.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

public class Util {

  public static final ArrayList<String> split(String var0, String var1) {
    if (var0 == null) {
      return new ArrayList<>(0);
    } else {
      if (var1 == null) {
        var1 = "~";
      }

      StringTokenizer var2 = new StringTokenizer(var0, var1, true);
      ArrayList<String> var3 = new ArrayList<>(var2.countTokens());

      String var4;
      String var5;
      for(var5 = null; var2.hasMoreTokens(); var5 = var4) {
        var4 = var2.nextToken();
        if (var1.equals(var4)) {
          if (var1.equals(var5)) {
            var3.add("");
          }
        } else {
          var3.add((String)var4);
        }
      }

      if (var1.equals(var5)) {
        var3.add("");
      }

      return var3;
    }
  }


  public static ArrayList<String> getStringList(Object obj) {
    if (obj instanceof String) {
      return new ArrayList<> (Collections.singleton((String) obj));
    } else if (obj instanceof ArrayList) {
      return (ArrayList<String>) obj;
    }
    return new ArrayList<>();
  }

}
