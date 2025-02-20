package tn.esprit.entities;

public enum Role {
    ADMIN, EVENT_PLANNER, TEAM_LEADER, SIMPLE_USER;

    @Override
    public String toString() {
        return this.name();
    }
}
