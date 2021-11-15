package servlets;


import java.io.IOException;


import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.*;
import services.UserService;

/**
 *
 * @author Apon Tran
 */
@WebServlet(name = "UserServlet", urlPatterns = {"/UserServlet"})
public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserService us = new UserService();

        String action = request.getParameter("action");

        if (action == null) {//show all users
            session.setAttribute("editMode", false);
            try {
                List<User> userList = us.getAll();
                session.setAttribute("userList", userList);
            } catch (Exception ex) {
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (action.equals("edit")) {
            session.setAttribute("editMode", true);
            String userPrimaryKey = request.getParameter("userPrimaryKey");
            User selectedUser = null;

            try {
                selectedUser = us.get(userPrimaryKey);
            } catch (Exception ex) {
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            request.setAttribute("selectedUser", selectedUser);
        }

        getServletContext().getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);
        return;

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        UserService us = new UserService();

        String action = request.getParameter("action");

        if (action.equals("add")) {
            String email = request.getParameter("email");
            String active_string = request.getParameter("active");
            boolean active = true;
            if (active_string == null) {
                active = false;
            }
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String password = request.getParameter("password");
            int role_id = Integer.parseInt(request.getParameter("role"));
            User user = new User(email, active, firstName, lastName, password);

            if (AllFieldsFilled(email, active, firstName, lastName, password, role_id)) {
                try {
                    us.insert(email, active, firstName, lastName, password, role_id);
                } catch (Exception ex) {
                    Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                request.setAttribute("user", user);
                request.setAttribute("role_id", role_id);
                request.setAttribute("errorMessage_EddUser", true);
                getServletContext().getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);
                return;
            }
        } else if (action.equals("edit")) {
            session.setAttribute("editMode", false);
            String Update_email = request.getParameter("update_email");
            String Update_firstName = request.getParameter("update_firstName");
            String Update_lastName = request.getParameter("update_lastName");
            String Update_password = request.getParameter("update_password");
            int Update_role_id = Integer.parseInt(request.getParameter("update_role"));
            
            String Update_active_string = request.getParameter("update_active");
            boolean Update_active = true;
            if (Update_active_string == null) {
                Update_active = false;
            }

            User Update_user = new User(Update_email, Update_active, Update_firstName, Update_lastName, Update_password);

            if (AllFieldsFilled(Update_email, Update_active, Update_firstName, Update_lastName, Update_password, Update_role_id)) {
                try {
                    us.update(Update_email, Update_active, Update_firstName, Update_lastName, Update_password, Update_role_id);
                } catch (Exception ex) {
                    Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                response.sendRedirect("users");
                return;
            } else {
                request.setAttribute("selectedUser", Update_user);
                request.setAttribute("errorMessage_EditUser", true);
                getServletContext().getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);
                return;
            }

        } else if (action.equals("delete")) {
            String userPrimaryKey = request.getParameter("userPrimaryKey");
            try {
                us.delete(userPrimaryKey);
                List<User> userList = us.getAll();
                session.setAttribute("userList", userList);

            } catch (Exception ex) {
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            getServletContext().getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);
            return;
        } else if (action.equals("cancel")) {
            session.setAttribute("editMode", false);
            response.sendRedirect("users");
            return;
        }

        try {
            List<User> userList = us.getAll();
            session.setAttribute("userList", userList);
        } catch (Exception ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        getServletContext().getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);
        return;

    }

    private boolean AllFieldsFilled(String email, boolean active, String firstName, String lastName, String password, int role) {
        if (email == null || email.equals("") || firstName == null || firstName.equals("")
                || lastName == null || lastName.equals("")
                || password == null || password.equals("")) {
            return false;
        }
        return true;
    }
}
