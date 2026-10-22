package interfaces;
import model.User;
import exception.InvalidUserException;
public interface UserOperations { void validate(User user) throws InvalidUserException; boolean authenticate(String username, String password); }
