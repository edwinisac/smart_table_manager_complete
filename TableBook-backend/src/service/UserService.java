package service;
import interfaces.UserOperations;
import model.User;
import exception.InvalidUserException;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.*;

/** Phase 2 service: interface implementation, validation and password hashing. */
public class UserService implements UserOperations {
    public void validate(User u) throws InvalidUserException {if(u==null)throw new InvalidUserException("User is required");if(u.getName()==null||u.getName().trim().isEmpty())throw new InvalidUserException("Name is required");if(u.getUsername()==null||u.getUsername().trim().length()<3)throw new InvalidUserException("Username must be at least 3 characters");if(u.getEmail()==null||!u.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))throw new InvalidUserException("Enter a valid email");if(u.getRole()==null||!(u.getRole().equals("USER")||u.getRole().equals("ADMIN")))throw new InvalidUserException("Invalid role");}
    public boolean authenticate(String username,String password){return username!=null&&password!=null;}
    public static String hash(String value){try{byte[] b=MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));StringBuilder s=new StringBuilder();for(byte x:b)s.append(String.format("%02x",x));return s.toString();}catch(Exception e){throw new IllegalStateException(e);}}
    public static boolean passwordMatches(String raw,String hash){return hash(raw).equals(hash);}
}
