package com.flipfit.business;

import com.flipfit.bean.*;
import com.flipfit.dao.GymAdminDAO;
import com.flipfit.dao.GymAdminDAOImpl;
import com.flipfit.exceptions.InvalidCredentialsException;
import com.flipfit.exceptions.ResourceNotFoundException;
import com.flipfit.exceptions.StatusUpdatedException;

import java.util.Scanner;
import java.util.*;

public class GymAdminBusinessImpl implements GymAdminBusiness {
    // Scanner instance for user input
    Scanner scanner = new Scanner(System.in);

    // DAO instance to interact with the database layer
    GymAdminDAO adminDAO = new GymAdminDAOImpl();

    // Method to view all gym bookings
    // Returns a list of GymBooking objects
    public List<GymBooking> viewBookings() {
        try {
            // Attempt to fetch the bookings from the DAO
            return adminDAO.viewBookings();
        } catch (ResourceNotFoundException e) {
            // Handle exception if no bookings are found
            System.out.println(e);
        }
        // Return an empty list in case of an exception
        return new ArrayList();
    }

    // Method to approve the gym owner's registration request
    // Takes requestId and status as input
    public void approveOwnerRegistration(int requestId, String statuss) {
        try {
            // Call DAO method to approve the request
            adminDAO.approveOwnerRegistration(requestId, statuss);
        } catch (StatusUpdatedException e) {
            // Handle exception if status cannot be updated
            System.out.println(e);
        } catch (ResourceNotFoundException e) {
            // Handle exception if resource is not found
            System.out.println(e);
        }
    }

    // Method to retrieve all pending gym owner registration requests
    // Returns a list of GymOwnerRequest objects
    public List<GymOwnerRequest> pendingRequests() {
        try {
            // Attempt to fetch the pending requests from DAO
            return adminDAO.pendingRequests();
        } catch (ResourceNotFoundException e) {
            // Handle exception if no requests are found
            System.out.println(e);
        }
        // Return an empty list in case of an exception
        return new ArrayList();
    }

    // Method to view the gym centers
    // Returns a list of GymCenter objects
    public List<GymCenter> viewCenter() {
        try {
            // Attempt to fetch the gym centers from DAO
            return adminDAO.viewCenter();
        } catch (ResourceNotFoundException e) {
            // Handle exception if no centers are found
            System.out.println(e);
        }
        // Return an empty list in case of an exception
        return new ArrayList();
    }

    // Method to update the admin's password
    // Takes email, new password, and role as input
    // Returns true if password is updated successfully, false otherwise
    @Override
    public boolean updatepwd(String email, String password, String role) {
        try {
            // Attempt to update the password through DAO
            return adminDAO.updatepwd(email, password, role);
        } catch (InvalidCredentialsException e) {
            // Handle exception if credentials are invalid
            System.out.println(e);
        }
        // Return false if an exception occurs
        return false;
    }
}
