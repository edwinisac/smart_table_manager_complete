import service.DataStore;
import model.User;
import service.UserService;
import java.nio.file.*;

/** Optional Phase 1 model demonstration. Run the web application through Tomcat. */
public class Main {
    public static void main(String[] args) throws Exception {
        Path data=Path.of(System.getenv().getOrDefault("TABLEBOOK_DATA_DIR","data"));
        DataStore store=new DataStore(data);
        if(args.length>0&&args[0].equals("demo")){
            User u=new User("demo","Demo User","student","student@example.com",UserService.hash("student123"),"USER","",true);
            System.out.println("Phase 1 User valid: "+u.isValid());
            System.out.println("JSON files: "+store.getDataDir().toAbsolutePath());
            return;
        }
        System.out.println("TableBook now uses Jakarta Servlets. Build the WAR with `mvn clean package` and deploy target/TableBook.war to Apache Tomcat 10.1+.");
    }
}
