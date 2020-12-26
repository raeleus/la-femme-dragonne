package com.ray3k.template.entities;

public interface Triggerable {
    String getTriggerName();
    void setTriggerName(String triggerName);
    void trigger();
}
