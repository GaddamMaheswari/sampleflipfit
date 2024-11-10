package com.flipfit.dao;

import com.flipfit.bean.GymOwner;
import com.flipfit.bean.GymSlots;
import com.flipfit.exceptions.DBConnectionException;
import com.flipfit.exceptions.DataEntryFailedException;
import com.flipfit.exceptions.InvalidCredentialsException;
import com.flipfit.exceptions.ResourceAlreadyExistsException;
import com.flipfit.utils.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class GymOwnerDAOImpl implements GymOwnerDAO {
    private Connection connection = null; // Connection object to interact with the database
    private PreparedStatement statement = null; // PreparedStatement for executing SQL queries

    @Override
    public boolean createProfile(GymOwner gymOwner) throws InvalidCredentialsException, DataEntryFailedException {
        try {
            // Establishing database connection
            connection = DBConnection.connect();

            // Checking if the user already exists with the given email address
            statement = connection.prepareStatement("SELECT * from Registration where EmailAddress = ?");
            statement.setString(1, gymOwner.getOwnerEmailAddress());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                throw new InvalidCredentialsException("User already exists with this email address");
            }

            // Adding user profile to the database
            System.out.println("Adding User Profile");
            statement = connection.prepareStatement("insert into User(`Name`,`Email`,`PhoneNumber`,`Role`,`Address`) values (?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, gymOwner.getOwnerName());
            statement.setString(2, gymOwner.getOwnerEmailAddress());
            statement.setString(3, gymOwner.getOwnerPhone());
            statement.setString(4, "gymowner");
            statement.setString(5, gymOwner.getOwnerAddress());
            int rowsAffected = statement.executeUpdate();
            int ownerId = 0;
            if (rowsAffected > 0) {
                // Retrieve the generated ownerId if the profile insertion is successful
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    ownerId = generatedKeys.getInt(1);
                }
            } else {
                throw new DataEntryFailedException("Failed Adding User Details to User Database");
            }

            // Inserting gym owner information into the OwnerInfo table
            statement = connection.prepareStatement("insert into OwnerInfo values (?,?,?,?,?,?)");
            statement.setInt(1, ownerId);
            statement.setString(2, gymOwner.getOwnerName());
            statement.setString(3, gymOwner.getOwnerEmailAddress());
            statement.setString(4, gymOwner.getOwnerAddress());
            statement.setString(5, gymOwner.getOwnerPhone());
            statement.setString(6, gymOwner.getPassword());
            statement.executeUpdate();

            // Inserting registration information into the Registration table
            statement = connection.prepareStatement("insert into Registration values (?,?,?,?)");
            statement.setInt(1, ownerId);
            statement.setString(2, gymOwner.getOwnerEmailAddress());
            statement.setString(3, gymOwner.getPassword());
            statement.setString(4, "gymowner");
            statement.executeUpdate();
            return true;
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (DBConnectionException e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public boolean registerCenter(int ownerId, String centerName, String location, int slots) throws DataEntryFailedException {
        try {
            // Establishing database connection
            connection = DBConnection.connect();

            // Inserting gym center registration request into OwnerRequest table
            statement = connection.prepareStatement("insert into OwnerRequest(`OwnerId`,`CenterName`,`CenterLocation`,`NumOfSlots`) values (?,?,?,?)");
            statement.setInt(1, ownerId);
            statement.setString(2, centerName);
            statement.setString(3, location);
            statement.setInt(4, slots);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected <= 0) {
                throw new DataEntryFailedException("Data Entry Failed into Owner Request Database");
            } else {
                return true;
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (DBConnectionException e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public boolean addSlots(int centerID, GymSlots slot) throws ResourceAlreadyExistsException, DataEntryFailedException {
        // Check if the slot already exists for the given gym center
        if (isSlotExists(centerID, slot)) {
            throw new ResourceAlreadyExistsException("Slot already exists for the given GymCenter, and given timings.");
        }

        // SQL query for adding a new slot
        String sql = "INSERT INTO Slots(`CenterId`,`StartTime`,`EndTime`,`NumOfSeats`,`Cost`) VALUES (?,?,?,?,?)";

        try {
            // Establishing database connection
            connection = DBConnection.connect();

            // Preparing and executing the SQL statement
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, centerID); // Assuming slot.getSlotID() retrieves the slot ID
            statement.setTime(2, Time.valueOf(slot.getStartTime())); // Assuming slot.getStarttime() returns LocalDateTime
            statement.setTime(3, Time.valueOf(slot.getEndTime())); // Assuming slot.getEndTime() returns a LocalTime object
            statement.setInt(4, slot.getTotalSeats());
            statement.setInt(5, slot.getCost()); // Assuming gymCenter.getGymID() retrieves the gymID

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted <= 0) {
                throw new DataEntryFailedException("Failed to add slot");
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DBConnectionException e) {
            System.out.println(e);
        }
        return false;
    }

    public boolean isSlotExists(int centerID, GymSlots slot) {
        // SQL query to check if the slot already exists in the Slots table
        String sql = "SELECT COUNT(*) AS count FROM Slots WHERE centerID = ? AND starttime = ? AND endtime = ?";
        try {
            connection = DBConnection.connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, centerID);
            statement.setTime(2, Time.valueOf(slot.getStartTime())); // Assuming slot.getStarttime() returns LocalDateTime
            statement.setTime(3, Time.valueOf(slot.getEndTime())); //

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DBConnectionException e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public boolean deleteSlot(int centerID, LocalTime starttime) throws DataEntryFailedException {
        // SQL query to delete a slot from the Slots table
        String sql = "DELETE FROM Slots WHERE centerID = ? AND starttime = ?";

        try {
            // Establishing database connection
            connection = DBConnection.connect();

            // Preparing and executing the SQL statement
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, centerID);
            statement.setTime(2, Time.valueOf(starttime));

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                return true;
            } else {
                throw new DataEntryFailedException("Slot not found or failed to delete");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DBConnectionException e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public boolean deleteCenter(int centerID) throws DataEntryFailedException {
        // SQL query to delete a gym center
        String sql = "DELETE FROM GymCenters WHERE centerID = ? ";

        try {
            // Establishing database connection
            connection = DBConnection.connect();

            // Preparing and executing the SQL statement
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, centerID);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                return true;
            } else {
                throw new DataEntryFailedException("Center not found or failed to delete");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DBConnectionException e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public boolean editProfile(GymOwner gymOwner) throws DataEntryFailedException {
        // SQL query to update gym owner's profile in the OwnerInfo table
        String sql = "UPDATE OwnerInfo SET Name = ?, Email = ?, Address = ?, PhoneNumber = ?, Password=? WHERE OwnerId = ?";

        try {
            // Establishing database connection
            connection = DBConnection.connect();

            // Preparing and executing the SQL statement
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, gymOwner.getOwnerName());
            statement.setString(2, gymOwner.getOwnerEmailAddress());
            statement.setString(3, gymOwner.getOwnerAddress());
            statement.setString(4, gymOwner.getOwnerPhone());
            statement.setString(5, gymOwner.getPassword());
            statement.setInt(6, gymOwner.getOwnerId());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated <= 0) {
                throw new DataEntryFailedException("Failed to update profile");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DBConnectionException e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public boolean bookSlot(int centerID, int slotID, LocalDateTime bookedTime, int numberOfSeats) throws DataEntryFailedException {
        // SQL query to book a slot
        String sql = "INSERT INTO SlotBookings(centerID, slotID, bookedTime, seatsBooked) VALUES(?, ?, ?, ?)";

        try {
            // Establishing database connection
            connection = DBConnection.connect();

            // Preparing and executing the SQL statement
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, centerID);
            statement.setInt(2, slotID);
            statement.setTimestamp(3, Timestamp.valueOf(bookedTime));
            statement.setInt(4, numberOfSeats);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted <= 0) {
                throw new DataEntryFailedException("Failed to book slot");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DBConnectionException e) {
            System.out.println(e);
        }
        return false;
    }
}
