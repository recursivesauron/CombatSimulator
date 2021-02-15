package com.rsauron.combatsimulator.mapper;

import com.rsauron.combatsimulator.Move;

import java.util.List;

public interface MoveMapper {
    public Move getMoveByName(String name);
    public List<Move> getMovesByCharacterName(String name);
}
