import java.security.*;
import java.io.File;
import java.io.FileInputStream;

public class ComputeSHA
{
  public static void main(String[] args) throws Exception
  {
    MessageDigest md = MessageDigest.getInstance("SHA-1");
    String filename = args[0];
    File file = new File(filename);
    byte[] buffer = new byte[(int)file.length()];
    FileInputStream fileStream = new FileInputStream(file);
    fileStream.read(buffer);
    fileStream.close();
    md.update(buffer);
    
    byte[] digest = md.digest();
    StringBuilder stringbuilder = new StringBuilder();
    for (byte b:digest){
      stringbuilder.append(String.format("%02x",b&0xff));
    }
    
    System.out.println(stringbuilder.toString());
  }
}
