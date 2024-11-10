package com.flipfit.business;

import com.flipfit.bean.GymBooking;
import com.flipfit.bean.GymCustomer;
import com.flipfit.bean.GymPayment;
import com.flipfit.dao.GymCustomerDAO;
import com.flipfit.dao.GymCustomerDAOImpl;
import com.flipfit.exceptions.DataEntryFailedException;
import com.flipfit.exceptions.InvalidCredentialsException;
import com.flipfit.exceptions.ResourceNotFoundException;
import com.flipfit.exceptions.UnauthorisedAccessException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class GymCustomerBusinessImpl implements GymCustomerBusiness {
    Scanner scanner = new Scanner(System.in);
    GymCustomerDAO custDAO = new GymCustomerDAOImpl();

    @Override
    public boolean createProfile(GymCustomer customer) {
        try {
            return custDAO.createProfile(customer);  // Create customer profile
        } catch (InvalidCredentialsException | DataEntryFailedException e) {
            System.out.println(e);
        }
        return false;  // Return false if creation fails
    }

    @Override
    public boolean editProfile(GymCustomer customer) {
        try {
            return custDAO.editProfile(customer);  // Edit customer profile
        } catch (DataEntryFailedException e) {
            System.out.println(e);
        }
        return false;  // Return false if editing fails
    }

    @Override
    public int createBooking(int customerId, int slotid, int centerId, Date date) {
        try {
            return custDAO.createBooking(customerId, slotid, centerId, date);  // Create a booking
        } catch (ResourceNotFoundException e) {
            System.out.println(e);
        }
        return -1;  // Return -1 if the booking creation fails
    }

    @Override
    public List<GymBooking> viewBookings(int customerId) {
        try {
            return custDAO.viewBookings(customerId);  // View all bookings for a specific customer
        } catch (ResourceNotFoundException e) {
            System.out.println(e);
        }
        return new ArrayList<>();  // Return an empty list if no bookings are found
    }

    @Override
    public boolean cancelBooking(int customerId, int bookingId) {
        try {
            return custDAO.cancelBooking(customerId, bookingId);  // Cancel a booking for a customer
        } catch (InvalidCredentialsException | UnauthorisedAccessException e) {
            System.out.println(e);
        }
        return false;  // Return false if the cancellation fails
    }

    @Override
    public int makepayment(int bookingId, String mode) {
        GymPayment gymPayment = new GymPayment(bookingId, mode);
        try {
            return custDAO.makepayment(gymPayment);  // Process payment for a booking
        } catch (DataEntryFailedException e) {
            System.out.println(e);
        }
        return -1;  // Return -1 if payment processing fails
    }

    @Override
    public boolean updatepwd(String email, String password, String role) {
        try {
            return custDAO.updatepwd(email, password, role);  // Update customer password
        } catch (InvalidCredentialsException e) {
            System.out.println(e);
        }
        return false;  // Return false if the password update fails
    }
}
