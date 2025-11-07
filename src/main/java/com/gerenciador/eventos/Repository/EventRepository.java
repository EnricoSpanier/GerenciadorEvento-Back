package com.gerenciador.eventos.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gerenciador.eventos.DatabaseConnection;
import com.gerenciador.eventos.POJO.Event;

/**
 * Repository para entidade Event usando JDBC cru via DatabaseConnection.
 * Mapeia o POJO Event para a tabela public.event.
 */
@Repository
public class EventRepository {

    @Autowired
    private DatabaseConnection databaseConnection;

    /**
     * Inserir novo evento.
     * Campos do banco: creator_id, event_name, ead, address, event_date,
     * buy_time_limit, capacity, quant, description.
     */
    public Event save(Event e) {
        if (e.getCreator_id() == null) {
            throw new IllegalArgumentException("creator_id é obrigatório");
        }
        if (e.getEvent_name() == null || e.getEvent_name().isBlank()) {
            throw new IllegalArgumentException("event_name é obrigatório");
        }
        if (e.getEvent_date() == null) {
            throw new IllegalArgumentException("event_date é obrigatório");
        }

            // buy_time_limit: definimos via aplicação
            String sql = "INSERT INTO event (creator_id, event_name, ead, address, event_date, buy_time_limit, capacity, quant, description) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = databaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                int idx = 1;
                stmt.setLong(idx++, e.getCreator_id());
                stmt.setString(idx++, e.getEvent_name());
                stmt.setBoolean(idx++, toPrimitive(e.getIs_EAD()));
                stmt.setString(idx++, nullIfBlank(e.getAddress()));
                stmt.setTimestamp(idx++, toTimestamp(e.getEvent_date()));
                // se buy_time_limit for nulo, usar event_date como default
                LocalDateTime buyLimit = (e.getBuy_time_limit() != null) ? e.getBuy_time_limit() : e.getEvent_date();
                stmt.setTimestamp(idx++, toTimestamp(buyLimit));
                if (e.getLot_quantity() == null) {
                    stmt.setNull(idx++, java.sql.Types.INTEGER);
                } else {
                    stmt.setInt(idx++, e.getLot_quantity());
                }
                stmt.setInt(idx++, e.getQuantity());
                stmt.setString(idx++, e.getDescription() != null ? e.getDescription() : "");

                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    e.setEvent_id(rs.getLong(1));
                }

                fetchTimestamps(e);
                return e;
            } catch (SQLException ex) {
                throw new RuntimeException("Erro ao salvar evento: " + ex.getMessage(), ex);
            }
    }

    /** Buscar evento por ID */
    public Event findById(Long id) {
        String sql = "SELECT * FROM event WHERE event_id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
            return null;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao buscar evento: " + ex.getMessage(), ex);
        }
    }

    /** Buscar por nome (único) */
    public Event findByName(String name) {
        String sql = "SELECT * FROM event WHERE event_name = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
            return null;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao buscar evento por nome: " + ex.getMessage(), ex);
        }
    }

    /** Verificar se existe evento com mesmo nome */
    public boolean nameExists(String name) {
        String sql = "SELECT COUNT(*) FROM event WHERE event_name = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao verificar nome do evento: " + ex.getMessage(), ex);
        }
    }

    /** Atualizar evento existente */
    public Event update(Event e) {
        if (e.getEvent_id() == null) {
            throw new IllegalArgumentException("event_id é obrigatório para atualizar");
        }
        // UPDATE: se buy_time_limit vier null, não atualizamos essa coluna (mantém valor atual)
        boolean updateBuyLimit = e.getBuy_time_limit() != null;
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE event SET creator_id = ?, event_name = ?, ead = ?, address = ?, event_date = ?, ");
        if (updateBuyLimit) {
            sb.append("buy_time_limit = ?, ");
        }
        sb.append("capacity = ?, quant = ?, description = ? WHERE event_id = ?");
        String upSql = sb.toString();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(upSql)) {

            int idx = 1;
            stmt.setLong(idx++, e.getCreator_id());
            stmt.setString(idx++, e.getEvent_name());
            stmt.setBoolean(idx++, toPrimitive(e.getIs_EAD()));
            stmt.setString(idx++, nullIfBlank(e.getAddress()));
            stmt.setTimestamp(idx++, toTimestamp(e.getEvent_date()));
            if (updateBuyLimit) {
                stmt.setTimestamp(idx++, toTimestamp(e.getBuy_time_limit()));
            }
            if (e.getLot_quantity() == null) {
                stmt.setNull(idx++, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(idx++, e.getLot_quantity());
            }
            stmt.setInt(idx++, e.getQuantity());
            stmt.setString(idx++, e.getDescription() != null ? e.getDescription() : "");
            long eventIdVal = Objects.requireNonNull(e.getEvent_id(), "event_id não pode ser nulo");
            stmt.setLong(idx++, eventIdVal);

            stmt.executeUpdate();
            fetchTimestamps(e);
            return e;
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao atualizar evento: " + ex.getMessage(), ex);
        }
    }

    /** Remover evento definitivamente */
    public void delete(Long id) {
        String sql = "DELETE FROM event WHERE event_id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao deletar evento: " + ex.getMessage(), ex);
        }
    }

    // ===== Helpers =====
    private void fetchTimestamps(Event e) {
        if (e.getEvent_id() == null) return;
        String sql = "SELECT created_at, updated_at FROM event WHERE event_id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, e.getEvent_id());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Timestamp c = rs.getTimestamp("created_at");
                if (c != null) e.setCreatedAt(c.toLocalDateTime());
                Timestamp u = rs.getTimestamp("updated_at");
                if (u != null) e.setUpdatedAt(u.toLocalDateTime());
            }
        } catch (SQLException ex) {
            System.err.println("Aviso: não foi possível buscar timestamps do evento: " + ex.getMessage());
        }
    }

    private Event map(ResultSet rs) throws SQLException {
        Event e = new Event();
        e.setEvent_id(rs.getLong("event_id"));
        e.setCreator_id(rs.getLong("creator_id"));
        e.setEvent_name(rs.getString("event_name"));
        e.setIs_EAD(rs.getBoolean("ead"));
        e.setAddress(rs.getString("address"));
        Timestamp ed = rs.getTimestamp("event_date");
        if (ed != null) e.setEvent_date(ed.toLocalDateTime());
        Timestamp btl = rs.getTimestamp("buy_time_limit");
        if (btl != null) e.setBuy_time_limit(btl.toLocalDateTime());
        int capacity = rs.getInt("capacity");
        if (!rs.wasNull()) e.setLot_quantity(capacity);
        e.setQuantity(rs.getInt("quant"));
        e.setDescription(rs.getString("description"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) e.setCreatedAt(createdAt.toLocalDateTime());
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) e.setUpdatedAt(updatedAt.toLocalDateTime());
        // presenters não está no schema -> manter lista vazia ou a recebida externamente
        return e;
    }

    private boolean toPrimitive(Boolean b) {
        return Boolean.TRUE.equals(b);
    }

    private Timestamp toTimestamp(LocalDateTime dt) {
        return (dt == null) ? null : Timestamp.valueOf(dt);
    }

    private String nullIfBlank(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
