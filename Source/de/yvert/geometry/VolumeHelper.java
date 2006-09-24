// arch-tag: 07e90366-7c18-4c81-ac88-72e93c79a6e9
package de.yvert.geometry;

public class VolumeHelper
{

private static double calcDeterminant(double a00, double a01, double a10, double a11, double a20, double a21)
{
	double temp0 = a00 * a11 + a01 * a20 + a10 * a21;
	double temp1 = a00 * a21 + a01 * a10 + a11 * a20;
	return temp0 - temp1;
}

/*
 * Calculates the 4x4 determinant of four homogenized points (add 1 as
 * forth component).
 */
private static double calcDeterminant(Vector3 p0, Vector3 p1, Vector3 p2, Vector3 p3)
{
	double temp0 = calcDeterminant(p1.getV1(), p1.getV2(), p2.getV1(), p2.getV2(), p3.getV1(), p3.getV2());
	double temp1 = calcDeterminant(p0.getV1(), p0.getV2(), p2.getV1(), p2.getV2(), p3.getV1(), p3.getV2());
	double temp2 = calcDeterminant(p0.getV1(), p0.getV2(), p1.getV1(), p1.getV2(), p3.getV1(), p3.getV2());
	double temp3 = calcDeterminant(p0.getV1(), p0.getV2(), p1.getV1(), p1.getV2(), p2.getV1(), p2.getV2());
	temp0 = temp0 * p0.getV0();
	temp1 = temp1 * p1.getV0();
	temp2 = temp2 * p2.getV0();
	temp3 = temp3 * p3.getV0();
	return temp0 - temp1 + temp2 - temp3;
}

public static double signedVolume(Vector3 a, Vector3 b, Vector3 c, Vector3 d)
{
	return calcDeterminant(a, b, c, d);
}

private VolumeHelper()
{/*OK*/}

}
