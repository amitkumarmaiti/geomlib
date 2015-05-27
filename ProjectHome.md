geomlib is a collection of classes for 3D vector math, 3D model loading and manipulation, raytracing, and advanced geometric algorithms.

It provides classes for several geometric primitives, such as vectors (2D/3D/4D), rays, triangles, triangle meshes, parallelograms, spheres, tori, cones (open and closed), rings,  bezier patches, bounding boxes, and bounding spheres. It has classes for Quaternion and Matrix calculations.

It specifies an API for 3D camera models and provides several implementations: freelook, targeted, and snapshot cameras.

Furthermore, it contains classes to read the obj model text file format (for basic models).

Most of the geometric primitive classes support standard raytracing operations as well as freely programmable shading and texturing. Support classes for these operations are also included, such as texture coordinate calculation, point and area light sources, perlin noise, and a standard library for programmable shading.

Last but not least, it contains implementations for a number of algorithms: numerical root finding up to degree 4, simple and contour based polygon triangulation, 2D convex hull, 3D polygon clipping, smooth vertex normal calculation for triangle meshes, a red-black treeset, and quick-select.

All of that comes in a library package with no external dependencies (except JUnit for the unit tests, which can be omitted).