package com.BootWebapp.DAO;

import com.BootWebapp.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@Repository
public class UserSearchRepository {

    private static final String	GET_USERS_BY_NAME = "SELECT u.user_id, u.first_name, u.last_name, " +
            "    GROUP_CONCAT(DISTINCT ts.tech_stack ORDER BY ts.tech_stack SEPARATOR ', ') AS techstacks, " +
            "    GROUP_CONCAT(DISTINCT gp.project_name ORDER BY gp.project_name SEPARATOR ', ') AS project_names, " +
            "    GROUP_CONCAT(DISTINCT gp.project_url ORDER BY gp.project_url SEPARATOR ', ') AS project_urls " +
            "FROM users u " +
            "LEFT JOIN userknowtechstacks ut ON u.user_id = ut.user_id " +
            "LEFT JOIN techstacks ts ON ut.techstack_id = ts.techstack_id " +
            "LEFT JOIN githubprojects gp ON u.user_id = gp.user_id " +
            "WHERE LOWER(REPLACE(u.first_name, ' ', '')) = LOWER(REPLACE(?, ' ', '')) " +
            "    OR LOWER(REPLACE(u.last_name, ' ', '')) = LOWER(REPLACE(?, ' ', '')) " +
            "GROUP BY u.user_id, u.first_name, u.last_name " +
            "ORDER BY u.user_id ";

    private static final String GET_USER_BY_TECH_STACK = " SELECT u.user_id,u.first_name, u.last_name, " +
            "    GROUP_CONCAT(DISTINCT ts.tech_stack ORDER BY ts.tech_stack SEPARATOR ', ') AS techstacks, " +
            "    GROUP_CONCAT(DISTINCT gp.project_name ORDER BY gp.project_name SEPARATOR ', ') AS project_names, " +
            "    GROUP_CONCAT(DISTINCT gp.project_url ORDER BY gp.project_url SEPARATOR ', ') AS project_urls " +
            "FROM users u " +
            "LEFT JOIN userknowtechstacks ut ON u.user_id = ut.user_id " +
            "LEFT JOIN techstacks ts ON ut.techstack_id = ts.techstack_id " +
            "LEFT JOIN githubprojects gp ON u.user_id = gp.user_id " +
            "WHERE u.user_id IN (" +
            "        SELECT u2.user_id " +
            "        FROM users u2 " +
            "        JOIN userknowtechstacks ut2 ON u2.user_id = ut2.user_id " +
            "        JOIN techstacks ts2 ON ut2.techstack_id = ts2.techstack_id " +
            "        WHERE LOWER(REPLACE(ts2.tech_stack, ' ', '')) = LOWER(REPLACE(?, ' ', ''))) " +
            "GROUP BY u.user_id, u.first_name, u.last_name " +
            "ORDER BY u.user_id";

    private static final String GET_USER_BY_EMAIL = "SELECT  u.user_id, u.first_name, u.last_name, " +
            "GROUP_CONCAT(DISTINCT ts.tech_stack ORDER BY ts.tech_stack SEPARATOR ', ') AS techstacks, " +
            "GROUP_CONCAT(DISTINCT gp.project_name ORDER BY gp.project_name SEPARATOR ', ') AS project_names, " +
            "GROUP_CONCAT(DISTINCT gp.project_url ORDER BY gp.project_url SEPARATOR ', ') AS project_urls " +
            "FROM  users u " +
            "LEFT JOIN userknowtechstacks ut ON u.user_id = ut.user_id " +
            "LEFT JOIN techstacks ts ON ut.techstack_id = ts.techstack_id " +
            "LEFT JOIN githubprojects gp ON u.user_id = gp.user_id " +
            "WHERE u.email = ?";


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserSearchRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private final User extractUser(ResultSet rs){

        try {
            User user = new User(rs.getInt("u.user_id"),
                    rs.getString("u.first_name"),
                    rs.getString("u.last_name"));

            String[] techStacks = rs.getString("techstacks") != null ?
                    rs.getString("techstacks").split(",") : null;

            String[] gitProjectNames = rs.getString("project_names") != null ?
                    rs.getString("project_names").split(",") : null;

            String[] gitProjectUrls = rs.getString("project_urls") != null ?
                    rs.getString("project_urls").split(",") : null;

            HashMap<String, String> map = new HashMap<>();

            int size = gitProjectNames != null ? gitProjectNames.length : 0;

            for (int i = 0; i < size; i++) {
                map.put(gitProjectNames[i], gitProjectUrls[i]);
            }

            user.setUserDetails(techStacks != null ? List.of(techStacks) : null, map);

            return user;
        }catch (SQLException sqlException){
            System.out.println("Extraction Error from ResultSet : "+ sqlException.getMessage());
            sqlException.printStackTrace();
            return null;
        }

    }

    public List<User> byName(String name){

        return jdbcTemplate.query(GET_USERS_BY_NAME , (rs, rowNum) -> extractUser(rs), name, name);

    }

    public User byEmail(String email){

        return jdbcTemplate.query(GET_USER_BY_EMAIL , (rs) ->{
            if(rs.next()){
                return extractUser(rs);
            }else
                return null;
        }, email);

    }

    public List<User> byTechStack(String techStack){

        return jdbcTemplate.query(GET_USER_BY_TECH_STACK , (rs,row) -> extractUser(rs) , techStack);

    }
}
