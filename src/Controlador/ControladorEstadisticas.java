package Controlador;

import modelo.ConexionBD;
import java.sql.*;
import java.util.*;

public class ControladorEstadisticas {

    private Connection getCon() {
        try {
            if (ConexionBD.conexion == null || ConexionBD.conexion.isClosed()) {
                ConexionBD.desconectar();
                ConexionBD.getInstance();
            }
        } catch (Exception e) {
            ConexionBD.desconectar();
            ConexionBD.getInstance();
        }
        return ConexionBD.conexion;
    }

    private void closeStmt(Statement s) {
        try { if (s != null) s.close(); } catch (Exception ignored) {}
    }
    private void closeRs(ResultSet r) {
        try { if (r != null) r.close(); } catch (Exception ignored) {}
    }

    public int[] getRoomStats() {
        int[] stats = new int[]{0, 0, 0, 0};
        String sql = "SELECT COUNT(*) as total, " +
            "COALESCE(SUM(CASE WHEN estadohabitacion='DISPONIBLE' THEN 1 ELSE 0 END), 0) as disp, " +
            "COALESCE(SUM(CASE WHEN estadohabitacion='OCUPADA' THEN 1 ELSE 0 END), 0) as ocup, " +
            "COALESCE(SUM(CASE WHEN estadohabitacion='MANTENIMIENTO' THEN 1 ELSE 0 END), 0) as mant " +
            "FROM habitaciones";
        Connection c = getCon();
        if (c == null) return stats;
        Statement s = null; ResultSet rs = null;
        try { s = c.createStatement(); rs = s.executeQuery(sql); if (rs.next()) { stats[0]=rs.getInt("total"); stats[1]=rs.getInt("disp"); stats[2]=rs.getInt("ocup"); stats[3]=rs.getInt("mant"); } }
        catch (Exception e) { System.err.println("roomStats: "+e.getMessage()); }
        finally { closeRs(rs); closeStmt(s); }
        return stats;
    }

    public int getActiveReservations() {
        Connection c = getCon(); if (c == null) return 0;
        Statement s = null; ResultSet rs = null;
        try { s = c.createStatement(); rs = s.executeQuery("SELECT COUNT(*) FROM reserva WHERE CURRENT_DATE BETWEEN fechaentrada AND fechasalida"); if (rs.next()) return rs.getInt(1); }
        catch (Exception e) { System.err.println("activeRes: "+e.getMessage()); }
        finally { closeRs(rs); closeStmt(s); }
        return 0;
    }

    public int getTotalClients() {
        Connection c = getCon(); if (c == null) return 0;
        Statement s = null; ResultSet rs = null;
        try { s = c.createStatement(); rs = s.executeQuery("SELECT COUNT(*) FROM cliente"); if (rs.next()) return rs.getInt(1); }
        catch (Exception e) { System.err.println("totalClients: "+e.getMessage()); }
        finally { closeRs(rs); closeStmt(s); }
        return 0;
    }

    public int getTotalEmployees() {
        Connection c = getCon(); if (c == null) return 0;
        Statement s = null; ResultSet rs = null;
        try { s = c.createStatement(); rs = s.executeQuery("SELECT COUNT(*) FROM empleado"); if (rs.next()) return rs.getInt(1); }
        catch (Exception e) { System.err.println("totalEmps: "+e.getMessage()); }
        finally { closeRs(rs); closeStmt(s); }
        return 0;
    }

    public double getTotalRevenue() {
        Connection c = getCon(); if (c == null) return 0;
        Statement s = null; ResultSet rs = null;
        try { s = c.createStatement(); rs = s.executeQuery("SELECT COALESCE(SUM(totalfactura), 0) FROM facturas"); if (rs.next()) return rs.getDouble(1); }
        catch (Exception e) { System.err.println("totalRevenue: "+e.getMessage()); }
        finally { closeRs(rs); closeStmt(s); }
        return 0;
    }

    public int getPendingInvoices() {
        Connection c = getCon(); if (c == null) return 0;
        Statement s = null; ResultSet rs = null;
        try { s = c.createStatement(); rs = s.executeQuery("SELECT COUNT(*) FROM facturas WHERE estadofactura='PENDIENTE'"); if (rs.next()) return rs.getInt(1); }
        catch (Exception e) { System.err.println("pendingInvoices: "+e.getMessage()); }
        finally { closeRs(rs); closeStmt(s); }
        return 0;
    }

    public int getTotalReservations() {
        Connection c = getCon(); if (c == null) return 0;
        Statement s = null; ResultSet rs = null;
        try { s = c.createStatement(); rs = s.executeQuery("SELECT COUNT(*) FROM reserva"); if (rs.next()) return rs.getInt(1); }
        catch (Exception e) { System.err.println("totalRes: "+e.getMessage()); }
        finally { closeRs(rs); closeStmt(s); }
        return 0;
    }

    public int getCheckInsToday() {
        Connection c = getCon(); if (c == null) return 0;
        Statement s = null; ResultSet rs = null;
        try { s = c.createStatement(); rs = s.executeQuery("SELECT COUNT(*) FROM reserva WHERE fechaentrada = CURRENT_DATE"); if (rs.next()) return rs.getInt(1); }
        catch (Exception e) { System.err.println("checkIns: "+e.getMessage()); }
        finally { closeRs(rs); closeStmt(s); }
        return 0;
    }

    public int getCheckOutsToday() {
        Connection c = getCon(); if (c == null) return 0;
        Statement s = null; ResultSet rs = null;
        try { s = c.createStatement(); rs = s.executeQuery("SELECT COUNT(*) FROM reserva WHERE fechasalida = CURRENT_DATE"); if (rs.next()) return rs.getInt(1); }
        catch (Exception e) { System.err.println("checkOuts: "+e.getMessage()); }
        finally { closeRs(rs); closeStmt(s); }
        return 0;
    }

    public double getRevenueDay() {
        Connection c = getCon(); if (c == null) return 0;
        Statement s = null; ResultSet rs = null;
        try { s = c.createStatement(); rs = s.executeQuery("SELECT COALESCE(SUM(totalfactura), 0) FROM facturas WHERE fechafactura = CURRENT_DATE"); if (rs.next()) return rs.getDouble(1); }
        catch (Exception e) { System.err.println("revenueDay: "+e.getMessage()); }
        finally { closeRs(rs); closeStmt(s); }
        return 0;
    }

    public double getRevenueWeek() {
        Connection c = getCon(); if (c == null) return 0;
        Statement s = null; ResultSet rs = null;
        try { s = c.createStatement(); rs = s.executeQuery("SELECT COALESCE(SUM(totalfactura), 0) FROM facturas WHERE fechafactura >= CURRENT_DATE - 7"); if (rs.next()) return rs.getDouble(1); }
        catch (Exception e) { System.err.println("revenueWeek: "+e.getMessage()); }
        finally { closeRs(rs); closeStmt(s); }
        return 0;
    }

    public double getRevenueMonth() {
        Connection c = getCon(); if (c == null) return 0;
        Statement s = null; ResultSet rs = null;
        try { s = c.createStatement(); rs = s.executeQuery("SELECT COALESCE(SUM(totalfactura), 0) FROM facturas WHERE EXTRACT(MONTH FROM fechafactura)=EXTRACT(MONTH FROM CURRENT_DATE) AND EXTRACT(YEAR FROM fechafactura)=EXTRACT(YEAR FROM CURRENT_DATE)"); if (rs.next()) return rs.getDouble(1); }
        catch (Exception e) { System.err.println("revenueMonth: "+e.getMessage()); }
        finally { closeRs(rs); closeStmt(s); }
        return 0;
    }

    public LinkedHashMap<String, Double> getMonthlyRevenue12() {
        LinkedHashMap<String, Double> map = new LinkedHashMap<>();
        Connection c = getCon(); if (c == null) return map;
        Statement s = null; ResultSet rs = null;
        try { s = c.createStatement(); rs = s.executeQuery("SELECT TO_CHAR(fechafactura, 'YYYY-MM') as mes, COALESCE(SUM(totalfactura), 0) as total FROM facturas WHERE fechafactura >= CURRENT_DATE - INTERVAL '12 months' GROUP BY mes ORDER BY mes"); while (rs.next()) map.put(rs.getString("mes"), rs.getDouble("total")); }
        catch (Exception e) { System.err.println("monthlyRev12: "+e.getMessage()); }
        finally { closeRs(rs); closeStmt(s); }
        return map;
    }

    public LinkedHashMap<String, Integer> getOccupancyByType() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        Connection c = getCon(); if (c == null) return map;
        Statement s = null; ResultSet rs = null;
        try { s = c.createStatement(); rs = s.executeQuery("SELECT h.tipohabitacion, COUNT(*) as total FROM reserva r JOIN habitaciones h ON r.idhabitacion = h.idhabitacion GROUP BY h.tipohabitacion ORDER BY total DESC"); while (rs.next()) map.put(rs.getString("tipohabitacion"), rs.getInt("total")); }
        catch (Exception e) { System.err.println("occupancyType: "+e.getMessage()); }
        finally { closeRs(rs); closeStmt(s); }
        return map;
    }

    public List<String[]> getUpcomingReservations() {
        List<String[]> list = new ArrayList<>();
        Connection c = getCon(); if (c == null) return list;
        Statement s = null; ResultSet rs = null;
        try { s = c.createStatement(); rs = s.executeQuery("SELECT r.idreserva, COALESCE(c.nombre || ' ' || c.apellido, 'N/A') as cliente, TO_CHAR(r.fechaentrada, 'YYYY-MM-DD') as entrada, TO_CHAR(r.fechasalida, 'YYYY-MM-DD') as salida, r.habitacion, r.personas FROM reserva r LEFT JOIN cliente c ON r.idcliente = c.idcliente WHERE r.fechaentrada BETWEEN CURRENT_DATE AND CURRENT_DATE + 7 ORDER BY r.fechaentrada"); while (rs.next()) list.add(new String[]{String.valueOf(rs.getInt("idreserva")), rs.getString("cliente"), rs.getString("entrada"), rs.getString("salida"), rs.getString("habitacion"), String.valueOf(rs.getInt("personas"))}); }
        catch (Exception e) { System.err.println("upcomingRes: "+e.getMessage()); }
        finally { closeRs(rs); closeStmt(s); }
        return list;
    }

    public List<String[]> getRecentInvoices() {
        List<String[]> list = new ArrayList<>();
        Connection c = getCon(); if (c == null) return list;
        Statement s = null; ResultSet rs = null;
        try { s = c.createStatement(); rs = s.executeQuery("SELECT idfactura, TO_CHAR(fechafactura, 'YYYY-MM-DD') as fecha, totalfactura, estadofactura, metodopago FROM facturas ORDER BY fechafactura DESC LIMIT 10"); while (rs.next()) list.add(new String[]{String.valueOf(rs.getInt("idfactura")), rs.getString("fecha"), String.format("$%,.0f", rs.getDouble("totalfactura")), rs.getString("estadofactura"), rs.getString("metodopago")}); }
        catch (Exception e) { System.err.println("recentInvoices: "+e.getMessage()); }
        finally { closeRs(rs); closeStmt(s); }
        return list;
    }
}
