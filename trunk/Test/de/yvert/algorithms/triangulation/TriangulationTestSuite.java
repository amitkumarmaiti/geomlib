package de.yvert.algorithms.triangulation;

import junit.framework.TestSuite;

public final class TriangulationTestSuite extends TestSuite
{

public TriangulationTestSuite()
{
	addTest(new TestSuite(BasicPolygonTest.class));
	addTest(new TestSuite(ContourTriangulationTest.class));
	addTest(new TestSuite(PolygonTriangulationEdgesTest.class));
	addTest(new TestSuite(PolygonTriangulationTrianglesTest.class));
}

public static TestSuite suite()
{ return new TriangulationTestSuite(); }

}
