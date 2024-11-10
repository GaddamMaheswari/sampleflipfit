package com.flipfit.business;

import com.flipfit.bean.GymCenter;
import com.flipfit.bean.GymOwner;
import com.flipfit.bean.GymSlots;
import com.flipfit.dao.GymAdminDAO;
import com.flipfit.dao.GymAdminDAOImpl;
import com.flipfit.dao.GymOwnerDAO;
import com.flipfit.dao.GymOwnerDAOImpl;
import com.flipfit.exceptions.DataEntryFailedException;
import com.flipfit.exceptions.InvalidCredentialsException;
import com.flipfit.exceptions.ResourceAlreadyExistsException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

public class GymOwnerBusinessImpl implements GymOwnerBusiness {

    // Create an instance of Scanner for input, and GymOwnerDAO for database interaction
    Scanner scanner = new Scanner(System.in);
    GymOwnerDAO ownerDAO = new GymOwnerDAOImpl();

    // Registers a new gym center for the specified owner
    // @param ownerId - the ID of the gym owner
    // @param centerName - the name of the gym center
    // @param location - the location of the gym center
    // @param slots - the number of slots available at the center
    @Override
    public boolean registerCenter(int ownerId, String centerName, String location, int slots) {
        try {
            return ownerDAO.registerCenter(ownerId, centerName, location, slots);
        } catch (DataEntryFailedException e) {
            System.out.println(e);
        }
        return false;
    }

    // Adds a new slot to an existing gym center
    // @param centerId - the ID of the gym center to which the slot is added
    // @param slot - the GymSlots object containing details of the new slot (start time, end time, cost)
    @Override
    public boolean addnewSlot(int centerId, GymSlots slot) {
        try {
            ownerDAO.addSlots(centerId, slot);
        } catch (ResourceAlreadyExistsException e) {
            System.out.println(e);
        } catch (DataEntryFailedException e) {
            System.out.println(e);
        }
        return false;
    }

    // Deletes an existing slot from a gym center based on start time
    // @param centerId - the ID of the gym center
    // @param startTime - the start time of the slot to be deleted
    @Override
    public boolean deleteSlot(int centerId, LocalTime startTime) {
        try {
            return ownerDAO.deleteSlot(centerId, startTime);
        } catch (DataEntryFailedException e) {
            System.out.println(e);
        }
        return false;
    }

    // Deletes a gym center
    // @param centerId - the ID of the gym center to be deleted
    @Override
    public boolean deleteCenter(int centerId) {
        try {
            return ownerDAO.deleteCenter(centerId);
        } catch (DataEntryFailedException e) {
            System.out.println(e);
        }
        return false;
    }

    // Allows the gym owner to edit their profile details
    // @param owner - the GymOwner object containing the updated profile information
    @Override
    public boolean editProfile(GymOwner owner) {
        try {
            return ownerDAO.editProfile(owner);
        } catch (DataEntryFailedException e) {
            System.out.println(e);
        }
        return false;
    }

    // Allows the gym owner to create a new profile
    // @param owner - the GymOwner object containing the profile details to be created
    @Override
    public boolean createProfile(GymOwner owner) {
        try {
            return ownerDAO.createProfile(owner);
        } catch (DataEntryFailedException e) {
            System.out.println(e);
        } catch (InvalidCredentialsException e) {
            System.out.println(e);
        }
        return false;
    }

    // Updates the password for the gym owner
    // @param email - the email of the gym owner
    // @param password - the new password to set
    // @param role - the role of the user (could be used for additional permission handling)
    @Override
    public boolean updatepwd(String email, String password, String role) {
        try {
            return ownerDAO.updatepwd(email, password, role);
        } catch (InvalidCredentialsException e) {
            System.out.println(e);
        }
        return false;
    }
}
