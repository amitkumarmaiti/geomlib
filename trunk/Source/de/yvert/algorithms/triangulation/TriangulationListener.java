// arch-tag: 74dc456c-758d-4315-8153-cfc2e1ac75de
package de.yvert.algorithms.triangulation;

public interface TriangulationListener
{

void reversing();
void addEdge(int a, int b);
void addTriangle(int a, int b, int c);

}
