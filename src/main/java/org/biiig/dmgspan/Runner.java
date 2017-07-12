package org.biiig.dmgspan;

import java.io.IOException;

public class Runner {
  public static void main(String[] args) throws IOException {
//    String input = Runner.class.getResource("syn.tlf").getFile();

//    System.out.println("Hello");

    new FSM().mine("/home/peet/syn.tlf");

  }
}
