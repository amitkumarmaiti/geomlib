// arch-tag: a456ef1b-6b73-418c-bb36-599bf0dd8e4a
package de.yvert.algorithms.triangulation;

enum VertexState
{

START         { @Override public VertexState reverse() { return SPLIT; } },
END           { @Override public VertexState reverse() { return MERGE; } },
REGULAR_LEFT  { @Override public VertexState reverse() { return REGULAR_RIGHT; } },
REGULAR_RIGHT { @Override public VertexState reverse() { return REGULAR_LEFT; } },
SPLIT         { @Override public VertexState reverse() { return START; } },
MERGE         { @Override public VertexState reverse() { return END; } };

public abstract VertexState reverse();

}