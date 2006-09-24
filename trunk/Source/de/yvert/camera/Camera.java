package de.yvert.camera;

import java.io.Serializable;

import de.yvert.geometry.Matrix3;
import de.yvert.geometry.Matrix4;
import de.yvert.geometry.Quaternion;
import de.yvert.geometry.Vector3;

/**
 * Describes an abstract camera interface.
 * 
 * @author Ulf Ochsenfahrt
 */
public interface Camera extends Cloneable, Serializable
{

Camera clone();

int getWidth();
int getHeight();
double getAspectRatio();
double getNearPlane();
double getFarPlane();
double getFieldOfViewY();
double getFieldOfViewX();
Vector3 getPosition(Vector3 result);
Quaternion getRotation(Quaternion result);
Quaternion getInverseRotation(Quaternion result);

void setWidth(int width);
void setHeight(int height);
void setAspectRatio(double aspectRatio);
void setNearPlane(double nearPlane);
void setFarPlane(double farPlane);
void setFieldOfViewY(double fieldOfViewY);
void setFieldOfViewX(double fieldOfViewX);
void setPosition(Vector3 position);
void setRotation(Quaternion rotation);

void setSize(int width, int height);

Matrix3 getMatrix3(Matrix3 result);
Matrix4 getMatrix4(Matrix4 result);
Matrix3 getInverseMatrix3(Matrix3 result);
Matrix4 getInverseMatrix4(Matrix4 result);

}
