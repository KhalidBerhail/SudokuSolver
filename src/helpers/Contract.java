package helpers;


public final class Contract
{
 private Contract() {
 }
 
 public static void checkWrongCondition(final boolean condition, final String... msg) {
     if (!condition) {
         return;
     }
     if (msg.length > 0) {
         throw new AssertionError((Object)msg[0]);
     }
     throw new AssertionError();
 }


}
