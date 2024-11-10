package com.flipfit.business;

import com.flipfit.bean.GymAdmin;
import com.flipfit.bean.GymCustomer;
import com.flipfit.bean.GymOwner;
import com.flipfit.dao.GymUserDAO;
import com.flipfit.dao.GymUserDAOImpl;
import com.flipfit.exceptions.DBConnectionException;
import com.flipfit.exceptions.InvalidCredentialsException;
import com.flipfit.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class GymUserBusinessImpl implements GymUserBusiness {

    // DAO instance to interact with the database
    GymUserDAO gymUserDAO = new GymUserDAOImpl();

    // Method to retrieve all customers from the database
    @Override
    public List<GymCustomer> viewAllCustomers() {
        try {
            // Fetching list of customers from the DAO
            return gymUserDAO.viewAllCustomers();
        } catch (ResourceNotFoundException e) {
            // Handling case when no resources are found
            System.out.println(e);
        }
        // Returning an empty list if exception occurs
        return new ArrayList();
    }

    // Method to retrieve all gym owners from the database
    @Override
    public List<GymOwner> viewAllGymOwners() {
        try {
            // Fetching list of gym owners from the DAO
            return gymUserDAO.viewAllGymOwners();
        } catch (ResourceNotFoundException e) {
            // Handling case when no resources are found
            System.out.println(e);
        }
        // Returning an empty list if exception occurs
        return new ArrayList();
    }

    // Method to perform user login, returns a user role identifier
    @Override
    public int login(String email, String password, String role) {
        try {
            // Calling DAO to verify login credentials and returning user role identifier
            return gymUserDAO.login(email, password, role);
        } catch (InvalidCredentialsException inex) {
            // Handling invalid credentials exception
            System.out.println(inex);
        } catch (DBConnectionException dbex){
            // Handling database connection exception
            System.out.println(dbex);
        }
        // Returning -1 if login fails
        return -1;
    }
}
