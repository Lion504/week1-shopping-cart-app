package com.shoppingcart;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    private CartService cartService = new CartService();

    @Test
    void testSaveCartRecord_Success() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDb = mockStatic(DatabaseConnection.class)) {
            Connection mockConn = mock(Connection.class);
            PreparedStatement mockStmt = mock(PreparedStatement.class);
            ResultSet mockKeys = mock(ResultSet.class);

            mockedDb.when(DatabaseConnection::getConnection).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString(), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenReturn(mockStmt);
            when(mockStmt.getGeneratedKeys()).thenReturn(mockKeys);
            when(mockKeys.next()).thenReturn(true);
            when(mockKeys.getInt(1)).thenReturn(1);

            int recordId = cartService.saveCartRecord(3, 45.99, "en");

            assertEquals(1, recordId);
            verify(mockStmt).setInt(1, 3);
            verify(mockStmt).setDouble(2, 45.99);
            verify(mockStmt).setString(3, "en");
            verify(mockStmt).executeUpdate();
            verify(mockStmt).close();
            verify(mockConn).close();
        }
    }

    @Test
    void testSaveCartRecord_Failure() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDb = mockStatic(DatabaseConnection.class)) {
            Connection mockConn = mock(Connection.class);
            PreparedStatement mockStmt = mock(PreparedStatement.class);

            mockedDb.when(DatabaseConnection::getConnection).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString(), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenReturn(mockStmt);
            doThrow(new SQLException("DB error")).when(mockStmt).executeUpdate();

            int recordId = cartService.saveCartRecord(2, 20.0, "fi");

            assertEquals(-1, recordId);
            verify(mockStmt).close();
            verify(mockConn).close();
        }
    }

    @Test
    void testSaveCartItems_Success() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDb = mockStatic(DatabaseConnection.class)) {
            Connection mockConn = mock(Connection.class);
            PreparedStatement mockStmt = mock(PreparedStatement.class);

            mockedDb.when(DatabaseConnection::getConnection).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);

            List<Double> prices = Arrays.asList(10.0, 20.0, 15.5);
            List<Integer> quantities = Arrays.asList(2, 1, 3);
            List<Double> subtotals = Arrays.asList(20.0, 20.0, 46.5);

            cartService.saveCartItems(1, prices, quantities, subtotals);

            // Verify cartRecordId (parameter index 1) set three times
            verify(mockStmt, times(3)).setInt(eq(1), eq(1));

            // Item 1 (item number=1, price=10.0, quantity=2, subtotal=20.0)
            verify(mockStmt).setInt(eq(2), eq(1));
            verify(mockStmt).setDouble(eq(3), eq(10.0));
            verify(mockStmt).setInt(eq(4), eq(2));
            // Item 2 (item number=2, price=20.0, quantity=1, subtotal=20.0)
            verify(mockStmt).setInt(eq(2), eq(2));
            verify(mockStmt).setDouble(eq(3), eq(20.0));
            verify(mockStmt).setInt(eq(4), eq(1));
            // Item 3 (item number=3, price=15.5, quantity=3, subtotal=46.5)
            verify(mockStmt).setInt(eq(2), eq(3));
            verify(mockStmt).setDouble(eq(3), eq(15.5));
            verify(mockStmt).setInt(eq(4), eq(3));
            verify(mockStmt).setDouble(eq(5), eq(46.5));

            // Subtotals: verify counts
            verify(mockStmt, times(2)).setDouble(eq(5), eq(20.0)); // items 1 and 2
            verify(mockStmt).setDouble(eq(5), eq(46.5));           // item 3

            verify(mockStmt, times(3)).addBatch();
            verify(mockStmt).executeBatch();
            verify(mockStmt).close();
            verify(mockConn).close();
        }
    }

    @Test
    void testSaveCartItems_EmptyLists() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDb = mockStatic(DatabaseConnection.class)) {
            Connection mockConn = mock(Connection.class);
            PreparedStatement mockStmt = mock(PreparedStatement.class);

            mockedDb.when(DatabaseConnection::getConnection).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);

            cartService.saveCartItems(99, Arrays.asList(), Arrays.asList(), Arrays.asList());

            verify(mockStmt, never()).addBatch();
            verify(mockStmt).executeBatch();
            verify(mockStmt).close();
        }
    }

    @Test
    void testSaveCart_Success() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDb = mockStatic(DatabaseConnection.class)) {
            Connection mockConn = mock(Connection.class);
            PreparedStatement mockStmt = mock(PreparedStatement.class);
            ResultSet mockKeys = mock(ResultSet.class);

            mockedDb.when(DatabaseConnection::getConnection).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString(), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenReturn(mockStmt);
            when(mockStmt.getGeneratedKeys()).thenReturn(mockKeys);
            when(mockKeys.next()).thenReturn(true);
            when(mockKeys.getInt(1)).thenReturn(42);
            when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);

            List<Double> prices = Arrays.asList(5.0, 10.0);
            List<Integer> quantities = Arrays.asList(4, 2);
            List<Double> subtotals = Arrays.asList(20.0, 20.0);

            cartService.saveCart(2, 40.0, "sv", prices, quantities, subtotals);

            verify(mockStmt, times(2)).close();
            verify(mockConn, times(2)).close();
        }
    }

    @Test
    void testSaveCart_WhenRecordSaveFails() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDb = mockStatic(DatabaseConnection.class)) {
            Connection mockConn = mock(Connection.class);
            PreparedStatement mockStmt = mock(PreparedStatement.class);

            mockedDb.when(DatabaseConnection::getConnection).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString(), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenReturn(mockStmt);
            doThrow(new SQLException("DB error")).when(mockStmt).executeUpdate();

            List<Double> prices = Arrays.asList(5.0);
            List<Integer> quantities = Arrays.asList(1);
            List<Double> subtotals = Arrays.asList(5.0);

            cartService.saveCart(1, 5.0, "ja", prices, quantities, subtotals);

            verify(mockConn).close();
            verify(mockStmt).close();
            assertTrue(true);
        }
    }

    @Test
    void testSaveCartItems_Failure() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDb = mockStatic(DatabaseConnection.class)) {
            Connection mockConn = mock(Connection.class);
            PreparedStatement mockStmt = mock(PreparedStatement.class);

            mockedDb.when(DatabaseConnection::getConnection).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
            doThrow(new SQLException("Batch error")).when(mockStmt).executeBatch();

            List<Double> prices = Arrays.asList(10.0);
            List<Integer> quantities = Arrays.asList(1);
            List<Double> subtotals = Arrays.asList(10.0);

            cartService.saveCartItems(5, prices, quantities, subtotals);

            verify(mockStmt).close();
            verify(mockConn).close();
        }
    }
}
