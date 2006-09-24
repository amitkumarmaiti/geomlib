// arch-tag: dbb56335-ba94-42f4-95ba-538a7127b0d3
package de.yvert;

import junit.framework.TestSuite;
import de.yvert.algorithms.DaTreeSetTest;
import de.yvert.algorithms.triangulation.BasicPolygonTest;
import de.yvert.algorithms.triangulation.HorrorTriangulationTest;
import de.yvert.algorithms.triangulation.PolygonTriangulationEdgesTest;
import de.yvert.algorithms.triangulation.PolygonTriangulationTrianglesTest;
import de.yvert.geometry.ReflectionMatrixTest;

public final class GeometryTestSuite extends TestSuite
{

public GeometryTestSuite()
{
	addTest(new TestSuite(BasicPolygonTest.class));
	addTest(new TestSuite(PolygonTriangulationEdgesTest.class));
	addTest(new TestSuite(PolygonTriangulationTrianglesTest.class));
	addTest(new TestSuite(HorrorTriangulationTest.class));
	addTest(new TestSuite(DaTreeSetTest.class));
	addTest(new TestSuite(ReflectionMatrixTest.class));
}

public static TestSuite suite()
{ return new GeometryTestSuite(); }

}
