/**
 * Copyright (c) 2006-2011, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.lgna.story.resourceutilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import org.alice.ide.ast.NodeUtilities;
import org.lgna.project.ast.Argument;
import org.lgna.project.ast.ConstructorBlockStatement;
import org.lgna.project.ast.NamedUserConstructor;
import org.lgna.project.ast.JavaConstructor;
import org.lgna.project.ast.ConstructorInvocationStatement;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.JavaField;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserPackage;
import org.lgna.project.ast.ParameterAccess;
import org.lgna.project.ast.UserParameter;
import org.lgna.project.ast.JavaConstructorParameter;
import org.lgna.project.ast.SuperConstructorInvocationStatement;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.JavaType;
import org.lgna.story.resources.ModelResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.xml.XMLUtilities;

/**
 * @author dculyba
 *
 */
public class ModelResourceUtilities {
	
	public static String DEFAULT_PACKAGE = "";
	
	public static String getName(Class<?> modelResource)
	{
		return modelResource.getSimpleName();
	}
	
	public static BufferedImage getThumbnail(Class<?> modelResource)
	{
		String name = getName(modelResource);
		URL resourceURL = modelResource.getResource("resources/"+ name+".png");
		if (resourceURL != null)
		{
			try
			{
				BufferedImage image = ImageIO.read(resourceURL);
				return image;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			return null;
		}
		return null;
	}
	
	private static AxisAlignedBox getBoundingBoxFromXML(Document doc)
	{
		Element bboxElement = XMLUtilities.getSingleChildElementByTagName(doc.getDocumentElement(), "BoundingBox");
		if (bboxElement != null)
		{
			Element min = (Element)bboxElement.getElementsByTagName("Min").item(0);
			Element max = (Element)bboxElement.getElementsByTagName("Max").item(0);
			
			double minX = Double.parseDouble(min.getAttribute("x"));
			double minY = Double.parseDouble(min.getAttribute("y"));
			double minZ = Double.parseDouble(min.getAttribute("z"));
			
			double maxX = Double.parseDouble(max.getAttribute("x"));
			double maxY = Double.parseDouble(max.getAttribute("y"));
			double maxZ = Double.parseDouble(max.getAttribute("z"));
			
			AxisAlignedBox bbox = new AxisAlignedBox(minX, minY, minZ, maxX, maxY, maxZ);
			
			return bbox;
		}
		
		return null;
	}
	
	public static AxisAlignedBox getBoundingBox(Class<?> modelResource)
	{
		String name = getName(modelResource);
		try {
			InputStream is = modelResource.getResourceAsStream("resources/"+name+".xml");
			Document doc = XMLUtilities.read(is);
			return getBoundingBoxFromXML(doc);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<Class<?>> loadResourceJarFile(File resourceJar)
	{
		List<Class<?>> classes = new LinkedList<Class<?>>();
		List<String> classNames = new LinkedList<String>();
		try
		{
			ZipFile zip = new ZipFile(resourceJar);
			Enumeration<? extends ZipEntry> entries = zip.entries();
			while (entries.hasMoreElements())
			{
				ZipEntry entry = entries.nextElement();
				if (entry.getName().endsWith(".class"))
				{
					String className = entry.getName().replace('/', '.');
					int lastDot = className.lastIndexOf(".");
					String baseName = className.substring(0, lastDot);
					classNames.add(baseName);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		try
		{
			URL url = resourceJar.toURI().toURL();  
			URL[] urls = new URL[]{url};
			ClassLoader cl = new URLClassLoader(urls);
			
			for (String className : classNames)
			{
				Class<?> cls = cl.loadClass(className);
				classes.add(cls);
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return classes;
	}
	
	public static String getAliceClassName(Class<?> resourceClass)
	{
		String name = resourceClass.getSimpleName();
		int resourceIndex = name.indexOf("Resource");
		if (resourceIndex != -1)
		{
			name = name.substring(0, resourceIndex);
		}
		name = getClassNameFromName(name);
		return name;
	}
	
	public static UserPackage getAlicePackage(Class<?> resourceClass, Class<?> rootClass)
	{
		String resourcePackage = resourceClass.getPackage().getName();
		String rootPackage = rootClass.getPackage().getName();
		int rootIndex = resourcePackage.indexOf(rootPackage);
		if (rootIndex != -1)
		{
			resourcePackage = resourcePackage.substring(rootIndex+rootPackage.length());
			if (resourcePackage.startsWith("."))
			{
				resourcePackage = resourcePackage.substring(1);
			}
		}
		resourcePackage = DEFAULT_PACKAGE + resourcePackage;
		return new UserPackage(resourcePackage);
	}
	
	public static NamedUserConstructor createConstructorForResourceClass(Class<?> resourceClass, ConstructorParameterPair constructorAndParameter)
	{
		UserParameter parameter = new UserParameter("modelResource", resourceClass);
		ParameterAccess parameterAccessor = new ParameterAccess(parameter);
		Argument superArgument = new Argument(constructorAndParameter.getParameter(), parameterAccessor);
		ConstructorInvocationStatement superInvocation = new SuperConstructorInvocationStatement(constructorAndParameter.getConstructor(), superArgument);
		ConstructorBlockStatement blockStatement = new ConstructorBlockStatement(superInvocation);
		UserParameter[] parameters = {parameter};
		NamedUserConstructor constructor = new NamedUserConstructor(parameters, blockStatement);
		return constructor ;
	}
	
	public static NamedUserConstructor createConstructorForResourceField(Field resourceField, ConstructorParameterPair constructorAndParameter)
	{
		JavaField javaField = JavaField.getInstance(resourceField);
		FieldAccess fieldAccess = NodeUtilities.createStaticFieldAccess(javaField);
		Argument superArgument = new Argument(constructorAndParameter.getParameter(), fieldAccess);
		ConstructorInvocationStatement superInvocation = new SuperConstructorInvocationStatement(constructorAndParameter.getConstructor(), superArgument);
		ConstructorBlockStatement blockStatement = new ConstructorBlockStatement(superInvocation);
		UserParameter[] parameters = {};
		NamedUserConstructor constructor = new NamedUserConstructor(parameters, blockStatement);
		return constructor ;
	}
	
	private static ConstructorParameterPair getConstructorAndParameterForAliceClass(NamedUserType aliceType)
	{
		for (int i=0; i<aliceType.constructors.size(); i++)
		{
			NamedUserConstructor constructor = aliceType.constructors.get(i);
			if (constructor.parameters.size() == 1)
			{
				UserParameter parameter = constructor.parameters.get(0);
				if (parameter.getValueType().isAssignableTo(ModelResource.class))
				{
					return new ConstructorParameterPair(constructor, parameter);
				}
			}
		}
		return null;
	}
	
	private static ConstructorParameterPair getConstructorAndParameterForJavaClass(Class<?> javaClass)
	{
		JavaType javeType = JavaType.getInstance(javaClass);
		List<JavaConstructor> constructors = javeType.getDeclaredConstructors();
		for (JavaConstructor constructor : constructors)
		{
			List<JavaConstructorParameter> parameters = (List<JavaConstructorParameter>) constructor.getParameters();
			if (parameters.size() == 1)
			{
				JavaConstructorParameter parameter = parameters.get(0);
				JavaType javaType = parameter.getValueTypeDeclaredInJava();
				if (javaType.isAssignableTo(ModelResource.class))
				{
					return new ConstructorParameterPair(constructor, parameter);
				}
			}
		}
		return null;
	}
	
	private static String getClassNameFromName(String name)
	{
		StringBuilder sb = new StringBuilder();
		String[] nameParts = name.split("[_ ]");
		for (String part : nameParts)
		{
			sb.append(part.substring(0, 1).toUpperCase());
			sb.append(part.substring(1).toLowerCase());
		}
		return sb.toString();
		
	}
	
	//The Stack<Class<?>> classes is a stack of classes representing the hierarchy of the classes, with the parent class at the top of the stack
	private static ModelResourceTreeNode addNodes(Stack<Class<?>> classes, ModelResourceTreeNode root)
	{
		Class<?> rootClass = null;
		ModelResourceTreeNode currentNode = root;
		while (!classes.isEmpty())
		{
			Class<?> currentClass = classes.pop();
			//The root class is the one at the top of the stack, so grab it the first time around
			if (rootClass == null)
			{
				rootClass = currentClass;
			}
			ModelResourceTreeNode parentNode = currentNode;
			ModelResourceTreeNode classNode = null;
			if (resourceClassToNodeMap.containsKey(currentClass))
			{
				classNode = resourceClassToNodeMap.get(currentClass);
			}
			//Build a new ModelResourceTreeNode for the current class
			if (classNode == null)
			{
				NamedUserType aliceType = null;
				String aliceClassName = getAliceClassName(currentClass);
				UserPackage packageName = getAlicePackage(currentClass, rootClass);
				UserMethod[] methods = {};
				UserField[] fields = {};
				if (parentNode == null || parentNode.getTypeDeclaredInAlice() == null)
				{
					Class<?> parentClass = getModelClassForResourceClass(currentClass);
					ConstructorParameterPair parentConstructorAndParameter = getConstructorAndParameterForJavaClass(parentClass);
					NamedUserConstructor constructor = createConstructorForResourceClass(currentClass, parentConstructorAndParameter);
					NamedUserConstructor[] constructors = {constructor};
					aliceType = new NamedUserType(aliceClassName, packageName, parentClass, constructors, methods, fields);
				}
				else
				{
					NamedUserType parentType = parentNode.getTypeDeclaredInAlice();
					ConstructorParameterPair parentConstructorAndParameter = getConstructorAndParameterForAliceClass(parentType);
					NamedUserConstructor constructor = createConstructorForResourceClass(currentClass, parentConstructorAndParameter);
					NamedUserConstructor[] constructors = {constructor};
					aliceType = new NamedUserType(aliceClassName, packageName, parentType, constructors, methods, fields);
				}
				classNode = new ModelResourceTreeNode(aliceType, currentClass);
				resourceClassToNodeMap.put(currentClass, classNode);
				if (root == null) //if the root node passed in is null, assign it to be the node from the first class we process
				{
					root = classNode;
				}
				Field[] resourceConstants = currentClass.getFields();
				if (resourceConstants.length != 0)
				{
					for (Field f : resourceConstants)
					{
						String fieldClassName = getClassNameFromName(f.getName())+aliceClassName;
						NamedUserType parentType = classNode.getTypeDeclaredInAlice();
						ConstructorParameterPair parentConstructorAndParameter = getConstructorAndParameterForAliceClass(parentType);
						NamedUserConstructor constructor = createConstructorForResourceField(f, parentConstructorAndParameter);
						NamedUserConstructor[] constructors = {constructor};
						NamedUserType fieldType = new NamedUserType(fieldClassName, packageName, parentType, constructors, methods, fields);
						ModelResourceTreeNode fieldNode = new ModelResourceTreeNode(fieldType, currentClass);
						try
						{
							ModelResource resource = (ModelResource)f.get(null);
							fieldNode.setModelResource(resource);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						fieldNode.setParent(classNode);
						resourceClassToNodeMap.put(f, fieldNode);
					}
				}
				
			}
			classNode.setParent(parentNode);
			currentNode = classNode;
		}
		return root;
	}
	
	private static Map<Class<?>, Class<?>> resourceClassToModelClassMap = new HashMap<Class<?>, Class<?>>();
	private static Map<Object, ModelResourceTreeNode> resourceClassToNodeMap = new HashMap<Object, ModelResourceTreeNode>();
	
	public static Class<?> getModelClassForResourceClass(Class<?> resourceClass)
	{
		if( resourceClass.isAnnotationPresent( org.lgna.project.annotations.ResourceTemplate.class ) ) {
			org.lgna.project.annotations.ResourceTemplate resourceTemplate = resourceClass.getAnnotation( org.lgna.project.annotations.ResourceTemplate.class );
			return resourceTemplate.modelClass();
		}
		return null;
	}
	
	
	public static ModelResourceTreeNode createClassTree(List<Class<?>> classes)
	{
		ModelResourceTreeNode classNodes = null;
		for (Class<?> cls : classes)
		{
			Class<?> currentClass = cls;
			Stack<Class<?>> classStack = new Stack<Class<?>>();			
			Class<?>[] interfaces = null;
			while (currentClass != null)
			{
				classStack.push(currentClass);
				Class<?> modelClass = getModelClassForResourceClass(currentClass);
				if (modelClass != null)
				{
					if (!resourceClassToModelClassMap.containsKey(currentClass))
					{
						resourceClassToModelClassMap.put(currentClass, modelClass);
					}
					break;
				}
				
				interfaces = currentClass.getInterfaces();
				if (interfaces != null && interfaces.length > 0)
				{
					currentClass = interfaces[0];
				}
				else
				{
					currentClass = null;
				}
			}
			classNodes = addNodes(classStack, classNodes);
		}
		ModelResourceTreeNode topNode = new ModelResourceTreeNode(null, null);
		classNodes.setParent(topNode);
		classNodes.printTree();
		return topNode;
	}
	

	
}
