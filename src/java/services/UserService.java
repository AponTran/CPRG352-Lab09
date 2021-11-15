package services;

import dataaccess.RoleDB;
import dataaccess.UserDB;
import java.util.List;
import models.Role;
import models.User;

public class UserService {

    public List<User> getAll() throws Exception {
        UserDB userDB = new UserDB();
        List<User> users = userDB.getAll();
        return users;
    }

    public User get(String email) throws Exception {
        UserDB userDB = new UserDB();
        User user = userDB.get(email);
        return user;
    }
    
    public void insert(String email, boolean active, String firstName, String lastName, String password, int role_id) throws Exception {
        //create user without any role and update data to the table
        User user = new User(email, active, firstName, lastName, password);
        RoleDB roleDB = new RoleDB();
        Role role = roleDB.get(role_id);
        user.setRole(role);
        UserDB userDB = new UserDB();
        userDB.insert(user); 
    }

    public void update(String email, boolean active, String firstName, String lastName, String password, int role_id) throws Exception {
       
        
        UserDB userDB = new UserDB();
        User user = userDB.get(email);
        user.setActive(active);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        RoleDB roleDB = new RoleDB();
        Role role = roleDB.get(role_id); 
        user.setRole(role);
        userDB.update(user);
    }

    public void delete(String email) throws Exception {
        UserDB userDB = new UserDB();
        User user = userDB.get(email);
        userDB.delete(user);
    }
}
