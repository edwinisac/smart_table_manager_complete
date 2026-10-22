package service;

import util.Json;
import java.nio.file.*;
import java.io.IOException;
import java.util.*;

/** JSON-file persistence used instead of a database for the four-phase coursework project. */
public class DataStore {
    private final Path dataDir;
    public DataStore(Path dataDir) throws IOException { this.dataDir=dataDir; Files.createDirectories(dataDir); initialize("users.json", seedUsers()); initialize("tables.json", seedTables()); initialize("bookings.json", new ArrayList<>()); }
    private void initialize(String name,Object seed)throws IOException {Path p=dataDir.resolve(name);if(!Files.exists(p))write(name,seed);}
    private List<Object> seedUsers(){List<Object> a=new ArrayList<>();a.add(user("u-admin","TableBook Admin","admin","admin@tablebook.local","admin123","ADMIN","",true));a.add(user("u-demo","Demo User","user","user@tablebook.local","user123","USER","9876543210",true));return a;}
    private Map<String,Object> user(String id,String name,String username,String email,String password,String role,String phone,boolean active){Map<String,Object>m=new LinkedHashMap<>();m.put("id",id);m.put("name",name);m.put("username",username);m.put("email",email);m.put("passwordHash",UserService.hash(password));m.put("role",role);m.put("phone",phone);m.put("active",active);return m;}
    private List<Object> seedTables(){List<Object>a=new ArrayList<>();int[] seats={2,4,4,6,2,4,4,6,2,4,4,6};for(int i=0;i<seats.length;i++){Map<String,Object>m=new LinkedHashMap<>();m.put("id",i+1);m.put("name","Table "+(i+1));m.put("capacity",seats[i]);m.put("active",true);a.add(m);}return a;}
    public synchronized List<Object> read(String name)throws IOException {Object o=Json.parse(Files.readString(dataDir.resolve(name)));return Json.array(o);}
    public synchronized void write(String name,Object value)throws IOException {Files.writeString(dataDir.resolve(name),Json.stringify(value)+System.lineSeparator());}
    public synchronized List<Map<String,Object>> objects(String name)throws IOException {List<Map<String,Object>> out=new ArrayList<>();for(Object o:read(name))out.add(Json.object(o));return out;}
    public synchronized Map<String,Object> byId(String name,String id)throws IOException {for(Map<String,Object> m:objects(name))if(String.valueOf(m.get("id")).equals(id))return m;return null;}
    public Path getDataDir(){return dataDir;}
}
