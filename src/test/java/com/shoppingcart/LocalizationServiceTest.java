package com.shoppingcart;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocalizationServiceTest {

    private LocalizationService service = new LocalizationService();

    @Test
    void testGetLocalizationStrings_Success() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDb = mockStatic(DatabaseConnection.class)) {
            Connection mockConn = mock(Connection.class);
            PreparedStatement mockStmt = mock(PreparedStatement.class);
            ResultSet mockRs = mock(ResultSet.class);

            mockedDb.when(DatabaseConnection::getConnection).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
            when(mockStmt.executeQuery()).thenReturn(mockRs);
            when(mockRs.next()).thenReturn(true, true, false);
            when(mockRs.getString("key")).thenReturn("app.title", "label.language");
            when(mockRs.getString("value")).thenReturn("Shopping Cart", "Language");

            Map<String, String> result = service.getLocalizationStrings("en");

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Shopping Cart", result.get("app.title"));
            assertEquals("Language", result.get("label.language"));
            verify(mockRs).close();
            verify(mockStmt).close();
            verify(mockConn).close();
        }
    }

    @Test
    void testGetLocalizationStrings_EmptyResult() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDb = mockStatic(DatabaseConnection.class)) {
            Connection mockConn = mock(Connection.class);
            PreparedStatement mockStmt = mock(PreparedStatement.class);
            ResultSet mockRs = mock(ResultSet.class);

            mockedDb.when(DatabaseConnection::getConnection).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
            when(mockStmt.executeQuery()).thenReturn(mockRs);
            when(mockRs.next()).thenReturn(false);

            Map<String, String> result = service.getLocalizationStrings("fr");

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(mockRs).close();
            verify(mockStmt).close();
            verify(mockConn).close();
        }
    }

    @Test
    void testGetLocalizationStrings_DatabaseException() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDb = mockStatic(DatabaseConnection.class)) {
            Connection mockConn = mock(Connection.class);
            PreparedStatement mockStmt = mock(PreparedStatement.class);

            mockedDb.when(DatabaseConnection::getConnection).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Connection failed"));

            Map<String, String> result = service.getLocalizationStrings("en");

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(mockConn).close();
        }
    }

    @Test
    void testGetLocalizationStrings_NullResultSet() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDb = mockStatic(DatabaseConnection.class)) {
            Connection mockConn = mock(Connection.class);
            PreparedStatement mockStmt = mock(PreparedStatement.class);

            mockedDb.when(DatabaseConnection::getConnection).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
            when(mockStmt.executeQuery()).thenReturn(null);

            Map<String, String> result = service.getLocalizationStrings("de");

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(mockStmt).close();
            verify(mockConn).close();
        }
    }

    @Test
    void testGetLocalizationStrings_MultipleLanguages_MapsCorrectly() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDb = mockStatic(DatabaseConnection.class)) {
            Connection mockConn = mock(Connection.class);
            PreparedStatement mockStmt = mock(PreparedStatement.class);
            ResultSet mockRs = mock(ResultSet.class);

            mockedDb.when(DatabaseConnection::getConnection).thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
            when(mockStmt.executeQuery()).thenReturn(mockRs);
            when(mockRs.next()).thenReturn(true, false);
            when(mockRs.getString("key")).thenReturn("button.calculate");
            when(mockRs.getString("value")).thenReturn("Calculate");

            Map<String, String> result = service.getLocalizationStrings("sv");

            assertEquals("Calculate", result.get("button.calculate"));
            assertFalse(result.containsKey("app.title"));
        }
    }
}
