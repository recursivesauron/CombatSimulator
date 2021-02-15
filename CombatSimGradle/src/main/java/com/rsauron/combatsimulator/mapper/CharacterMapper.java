package com.rsauron.combatsimulator.mapper;

import com.rsauron.combatsimulator.Character;
import java.util.List;

public interface CharacterMapper {
    public List<Character> getAllCharacters();
    public List<Integer> getEnemiesByID(int id);
    public List<Integer> getAlliesByID(int id);
}
