package createApiGraphQL;
/*
 *  $Id: VirtuosoSPARQLExample2.java,v 1.3 2008/04/10 07:26:30 source Exp $
 *
 *  This file is part of the OpenLink Software Virtuoso Open-Source (VOS)
 *  project.
 *
 *  Copyright (C) 1998-2008 OpenLink Software
 *
 *  This project is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the
 *  Free Software Foundation; only version 2 of the License, dated June 1991.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 *
 */

import java.io.BufferedReader;

//package virtuoso.jena.driver;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;

import javax.lang.model.element.Modifier;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.commons.io.FileUtils;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import virtuoso.jena.driver.*;

@WebServlet(urlPatterns = {"/Main"})
public class Main extends HttpServlet{
	
	static String dbName, user, password, url_hostlist;
	
	//static private String fileDestination = Paths.get("./src/main/java").toAbsolutePath().normalize().toString();
	//static private String destinationPathApiGraphQL = Paths.get("./src/main/resources/esquema.graphqls").toAbsolutePath().normalize().toString();
	//static private String destinationPathApiGraphQL ;
	//static private String fileDestination;
	static public String fileDestination;
	static private URL destinationPathApiGraphQL = ClassLoader.getSystemResource("esquema.graphqls");

	
	static void getObjects(ArrayList<Object> objects, ArrayList<Field> fields, HashMap<String, ArrayList<String>> interfaces,  VirtGraph graph){
		

		Query sparql = QueryFactory.create("SELECT ?sujeto (group_concat(?subClass ; separator= \" \") as ?subClasses) FROM <"+ dbName +">  WHERE { " 
				+ "?sujeto <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.essi.upc.edu/~jvarga/gql/Object> ."
				+ "OPTIONAL {?sujeto <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?subClass .}"
				+ "}"
				+ "group by ?sujeto");


	    VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
	    ResultSet res = vqe.execSelect();

	    while(res.hasNext()){
	    	 ArrayList<Field> fieldsOfObject = new ArrayList<>();
	    	 QuerySolution qs = res.next();
	    	 for(Field f : fields){
	    		 if(f.getDomain().equals(qs.get("?sujeto").toString())) fieldsOfObject.add(f);
	    	 }
	    	 
	    	 ArrayList<String> subClasses = null;
	    	 
	    	 if(qs.get("?subClasses").toString().length() == 0)subClasses = new ArrayList<>();
	    	 else{
	    		 subClasses = new ArrayList<String>(Arrays.asList(qs.get("?subClasses").toString().split(" ")));
	    		 for(String subClass : subClasses){
	    			 if(interfaces.containsKey(subClass))interfaces.get(subClass).add(qs.get("?sujeto").toString());
	    			 else{
	    				 interfaces.put(subClass, new ArrayList<String>());
	    				 interfaces.get(subClass).add(qs.get("?sujeto").toString());
	    			 }
	    		 }
	    	 }
	    	 objects.add(new Object(qs.get("?sujeto").toString() , subClasses, fieldsOfObject ));
	    }
	}
	
	
	static ArrayList<Modificador> sortModifiers(String startNode, ArrayList<String> otherNodes, VirtGraph graph){
		ArrayList<Modificador> orderedModifiers = new ArrayList<>();
		
		for(int i = 0; i < otherNodes.size(); ++i){
			Query sparql = QueryFactory.create("SELECT ?rightNode ?rightNodeType  FROM <"+ dbName +"> WHERE { "
					+ "<" + startNode + "> <http://www.essi.upc.edu/~jvarga/gql/combinedWith> ?rightNode."
					+ "?rightNode <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?rightNodeType."
					+ "}");
			VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
		    ResultSet res = vqe.execSelect();


		    while(res.hasNext()){
		    	 QuerySolution qs = res.next();
		    	 if(qs.get("?rightNode") != null){
		    		 if(qs.get("?rightNodeType").toString().equals("http://www.essi.upc.edu/~jvarga/gql/List")) orderedModifiers.add(new List(qs.get("?rightNode").toString(), new ArrayList<Modificador>()));
		    		 else if(qs.get("?rightNodeType").toString().equals("http://www.essi.upc.edu/~jvarga/gql/NonNull")) orderedModifiers.add(new NonNull(qs.get("?rightNode").toString(), new ArrayList<Modificador>()));
		    		 startNode = qs.get("?rightNode").toString();
		    	 }
		    }
		}
		
		return orderedModifiers;
	}
	
	static ArrayList<String> getCombinedModifiers(String subject , VirtGraph graph){
		ArrayList<String> combinedModifiers = new ArrayList<>();
		Query sparql = QueryFactory.create("SELECT  (group_concat(?combinedWith; separator= \" \") as ?combinedModifiers) FROM <"+ dbName +"> WHERE { "
				+ "<" + subject + "> <http://www.essi.upc.edu/~jvarga/gql/combinedWith>+ ?combinedWith."
				+ "}"
				);

	    VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
	    ResultSet res = vqe.execSelect();
	    while(res.hasNext()){
	    	 QuerySolution qs = res.next();
	    	 if(qs.get("?combinedModifiers").toString().length() == 0)combinedModifiers = new ArrayList<>();
	    	 else combinedModifiers = new ArrayList<String>(Arrays.asList(qs.get("?combinedModifiers").toString().split(" ")));
	    }

		return combinedModifiers;
	}
	
	static void getFields(ArrayList<Field> createdField, VirtGraph graph){
		Query sparql = QueryFactory.create("SELECT ?sujetoScalarField ?sujetoObjectField ?domain ?range ?modifierType ?modifier (group_concat(?combinedWith; separator= \" \") as ?combinedModifiers) FROM <"+ dbName +"> WHERE { "
				+ "	{"
				+ "	OPTIONAL {"
				+ "		?sujetoScalarField <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/1999/02/22-rdf-syntax-ns#Property> ."
				+ "		?sujetoScalarField <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.essi.upc.edu/~jvarga/gql/ScalarField> ."
				+ "		?sujetoScalarField <http://www.w3.org/2000/01/rdf-schema#domain> ?domain ."
				+ "		?sujetoScalarField <http://www.w3.org/2000/01/rdf-schema#range> ?range ."
				+ "			OPTIONAL{"
				+ "				?sujetoScalarField <http://www.essi.upc.edu/~jvarga/gql/hasModifier> ?modifier."
				+ "				?modifier <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?modifierType."
				+ "			}"
				+ "		}"
				+ "	}"
				+ "UNION"
				+ "	{"
				+ "	OPTIONAL {"
				+ "		?sujetoObjectField <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/1999/02/22-rdf-syntax-ns#Property> ."
				+ "		?sujetoObjectField <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.essi.upc.edu/~jvarga/gql/ObjectField> ."
				+ "		?sujetoObjectField <http://www.w3.org/2000/01/rdf-schema#domain> ?domain ."
				+ "		?sujetoObjectField <http://www.w3.org/2000/01/rdf-schema#range> ?range ."
				+ "			OPTIONAL{"
				+ "				?sujetoObjectField <http://www.essi.upc.edu/~jvarga/gql/hasModifier> ?modifier."
				+ "				?modifier <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?modifierType."
				+ "			}"
				+ "		}"
				+ "	}"
				+ "}"
				+ "group by ?sujetoScalarField ?sujetoObjectField ?domain ?range ?modifierType ?modifier"
				);


	    VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
	    ResultSet res = vqe.execSelect();
	    





	    while(res.hasNext()){
	    	 QuerySolution qs = res.next();

	    	 String modifierType = null;
	    	 if(qs.get("?modifierType") != null) modifierType = qs.get("?modifierType").toString();

	    	 if(qs.get("?sujetoObjectField") != null){
	    		 if(modifierType != null){
	    			 ArrayList<Modificador> combinedModifiersOrdered = new ArrayList<>();
	    			 ArrayList<String> combinedModifiers = getCombinedModifiers(qs.get("?modifier").toString() , graph);
	    		 	 if(combinedModifiers.size() != 0) combinedModifiersOrdered = sortModifiers(qs.get("?modifier").toString(), combinedModifiers, graph);

	    		 	 //##ex:stopName  gql:hasModifier ex:nn1
	    		 	 //ex:nn1 a gql:NonNull . ---> modifier a modifierType
	    		 	 //ex:nn1 gql:combinedWith ex:l1 . --> combinedModifiersOrdered
	    			 if(modifierType.equals("http://www.essi.upc.edu/~jvarga/gql/List"))createdField.add(new ObjectField(qs.get("?sujetoObjectField").toString(),qs.get("?domain").toString(), qs.get("?range").toString(), new List(qs.get("?modifier").toString(), combinedModifiersOrdered)));
	    			 else if(modifierType.equals("http://www.essi.upc.edu/~jvarga/gql/NonNull"))createdField.add(new ObjectField(qs.get("?sujetoObjectField").toString(),qs.get("?domain").toString(), qs.get("?range").toString(), new NonNull(qs.get("?modifier").toString(), combinedModifiersOrdered)));

	    		 }else{
	    			 createdField.add(new ObjectField(qs.get("?sujetoObjectField").toString(),qs.get("?domain").toString(), qs.get("?range").toString(), null ));
	    		 }
	    	 }
	    	 else if(qs.get("?sujetoScalarField") != null){
	    		 if(modifierType != null){
	    			 ArrayList<Modificador> combinedModifiersOrdered = new ArrayList<>();
	    			 
	    			 ArrayList<String> combinedModifiers = getCombinedModifiers(qs.get("?modifier").toString() , graph);
	    			 if(combinedModifiers.size() != 0)combinedModifiersOrdered = sortModifiers(qs.get("?modifier").toString(), combinedModifiers, graph);
	    	
	    		 	 
	    			 if(modifierType.equals("http://www.essi.upc.edu/~jvarga/gql/List"))createdField.add(new ScalarField(qs.get("?sujetoScalarField").toString(),qs.get("?domain").toString(), qs.get("?range").toString(), new List(qs.get("?modifier").toString(), combinedModifiersOrdered)));
	    			 else if(modifierType.equals("http://www.essi.upc.edu/~jvarga/gql/NonNull"))createdField.add(new ScalarField(qs.get("?sujetoScalarField").toString(),qs.get("?domain").toString(), qs.get("?range").toString(), new NonNull(qs.get("?modifier").toString(), combinedModifiersOrdered)));

	    		 }else{
	    			 createdField.add(new ScalarField(qs.get("?sujetoScalarField").toString(),qs.get("?domain").toString(), qs.get("?range").toString(), null ));
	    		 }
	    	 }
	    }
	}
	
	static String constructRange(String range, Modificador mod){
		String combination = range;
		int contadorClaudators = 0;
		if(mod != null){
			if(mod.getClass().equals(List.class)){ combination = combination + "]"; ++contadorClaudators;}
			else if(mod.getClass().equals(NonNull.class)) combination = combination + "!";
			if(mod.getCombinedWith() != null && mod.getCombinedWith().size() > 0){		
				for(Modificador combined : mod.getCombinedWith()){		
					if(combined.getClass().equals(List.class)){ combination = combination + "]"; ++contadorClaudators;}
					else if(combined.getClass().equals(NonNull.class)) combination = combination + "!";
				}
			}
			while(contadorClaudators > 0){
				combination =  "[" + combination;
				--contadorClaudators;
			}
		}
		return combination;
	}
	
	static void writeFields(Object o , FileWriter fw, HashMap<String, ArrayList<String>> interfaces) throws IOException{
		for(Field f : o.getFields()){
    		Integer index = f.getName().lastIndexOf("/");
			String shortName = f.getName().substring(index + 1);
			
			index = f.getRange().lastIndexOf("/");
			String shortRange = f.getRange().substring(index + 1);
			if(interfaces.containsKey(f.getRange())) shortRange = "I" + shortRange;
			//construct Range [String!]
			String range = constructRange(shortRange, f.getModifier());
			
			fw.write("\t" + shortName + " : " + range + "\r\n");
		}
		
	}
	
	public static MethodSpec createModifyScalarValue(){
		
		MethodSpec method = MethodSpec.methodBuilder("modifyScalarValue")
				.addParameter(String.class, "value")
				.addCode("int index = value.toString().indexOf(\"^\");\n")
				.addCode("String resultat =  value.toString().substring(0, index);\n")
				.addCode("return resultat;\n")
				.addModifiers(Modifier.PUBLIC)
				.returns(String.class)
				.build();
		return method;
	}
	
	public static MethodSpec connectVirtuoso(){
		ClassName string = ClassName.get("java.lang", "String");
		ClassName arrayList = ClassName.get("java.util", "ArrayList");
		
		ClassName Query = ClassName.get("org.apache.jena.query", "Query");
		ClassName QueryFactory = ClassName.get("org.apache.jena.query", "QueryFactory");
		ClassName QuerySolution = ClassName.get("org.apache.jena.query", "QuerySolution");
		ClassName ResultSet = ClassName.get("org.apache.jena.query", "ResultSet");

		ClassName VirtGraph = ClassName.get("virtuoso.jena.driver", "VirtGraph");
		ClassName VirtuosoQueryExecution = ClassName.get("virtuoso.jena.driver", "VirtuosoQueryExecution");
		ClassName VirtuosoQueryExecutionFactory = ClassName.get("virtuoso.jena.driver", "VirtuosoQueryExecutionFactory");
		
		TypeName listOfStrings = ParameterizedTypeName.get(arrayList, string);
		
		
		MethodSpec method = MethodSpec.methodBuilder("connectVirtuoso")
				.addParameter(String.class, "value")
				.addCode("$T graph = new $T (\"$L\", \"$L\", \"$L\");\n", VirtGraph , VirtGraph, url_hostlist, user, password)
				.addCode("$T sparql = $T.create(\"Select ?valor FROM <$L> WHERE {\"\n", Query, QueryFactory, dbName)
				.addCode("+ \" <\"+ this.idTurtle +\"> <\"+  value + \"> ?valor.\"\n")
				.addCode("+ \"}\");\n \n")
				.addCode("$T vqe = $T.create (sparql, graph);\n",VirtuosoQueryExecution, VirtuosoQueryExecutionFactory)
				.addCode("$T res = vqe.execSelect();\n", ResultSet)
				.addCode("ArrayList<String> valor = new ArrayList<>();\n\n")
				.addCode("while(res.hasNext()){\n")
				.addCode("\t $T qs = res.next();\n", QuerySolution)
				.addCode("\t valor.add(qs.get(\"?valor\").toString());\n")
				.addCode("}\n\n")
				.addCode("graph.close();\n")
				.addCode("return valor;\n")
			    .returns(listOfStrings)
			    .addModifiers(Modifier.PUBLIC)
			    .build();
		return method;
	}
	
	public static MethodSpec connectVirtuosoWithId(){
		ClassName string = ClassName.get("java.lang", "String");
		ClassName arrayList = ClassName.get("java.util", "ArrayList");
		
		ClassName Query = ClassName.get("org.apache.jena.query", "Query");
		ClassName QueryFactory = ClassName.get("org.apache.jena.query", "QueryFactory");
		ClassName QuerySolution = ClassName.get("org.apache.jena.query", "QuerySolution");
		ClassName ResultSet = ClassName.get("org.apache.jena.query", "ResultSet");

		ClassName VirtGraph = ClassName.get("virtuoso.jena.driver", "VirtGraph");
		ClassName VirtuosoQueryExecution = ClassName.get("virtuoso.jena.driver", "VirtuosoQueryExecution");
		ClassName VirtuosoQueryExecutionFactory = ClassName.get("virtuoso.jena.driver", "VirtuosoQueryExecutionFactory");
		
		TypeName listOfStrings = ParameterizedTypeName.get(arrayList, string);
		
		
		MethodSpec method = MethodSpec.methodBuilder("connectVirtuoso")
				.addParameter(String.class, "value")
				.addParameter(String.class, "id")
				.addCode("$T graph = new $T (\"$L\", \"$L\", \"$L\");\n", VirtGraph , VirtGraph, url_hostlist, user, password)
				.addCode("$T sparql = $T.create(\"Select ?valor FROM <$L> WHERE {\"\n", Query, QueryFactory, dbName)
				.addCode("+ \" <\"+ id +\"> <\"+  value + \"> ?valor.\"\n")
				.addCode("+ \"}\");\n \n")
				.addCode("$T vqe = $T.create (sparql, graph);\n",VirtuosoQueryExecution, VirtuosoQueryExecutionFactory)
				.addCode("$T res = vqe.execSelect();\n", ResultSet)
				.addCode("ArrayList<String> valor = new ArrayList<>();\n\n")
				.addCode("while(res.hasNext()){\n")
				.addCode("\t $T qs = res.next();\n", QuerySolution)
				.addCode("\t valor.add(qs.get(\"?valor\").toString());\n")
				.addCode("}\n\n")
				.addCode("graph.close();\n")
				.addCode("return valor;\n")
			    .returns(listOfStrings)
			    .addModifiers(Modifier.PUBLIC)
			    .build();
		return method;
	}
	
	public static ClassName getClassName(String name){
		ClassName ClassName = null;
		if(name.equals("Int")) ClassName = ClassName.get("java.lang", "Integer");
		else if(name.equals("String")) ClassName = ClassName.get("java.lang", "String");
		else if(name.equals("Boolean")) ClassName = ClassName.get("java.lang", "Boolean");
		else if(name.equals("Float")) ClassName = ClassName.get("java.lang", "Float");
		else if(name.equals("ID")) ClassName = ClassName.get("java.lang", "Integer");
		else ClassName = ClassName.get("serverGraphQL", name);
		return ClassName;
	}

	public static void addFields(Object o, HashMap<String, ArrayList<String>> interfaces, ArrayList<MethodSpec> methods , boolean isInterface){
		for(Field field : o.getFields()){
			Integer index = field.getName().lastIndexOf("/");
			String nameField = field.getName().substring(index + 1);
			
			index = field.getRange().lastIndexOf("/");
			String rangeField = field.getRange().substring(index + 1);
			
			String rangeFieldNoModificado = rangeField;
			
			if(interfaces.containsKey(field.getRange())) rangeField = "I"+rangeField;
			
			ClassName className = getClassName(rangeField);
			ClassName arrayList = ClassName.get("java.util", "ArrayList");
			TypeName listOfClassName = ParameterizedTypeName.get(arrayList, className);
			TypeName nestedListOfClassName = ParameterizedTypeName.get(arrayList, listOfClassName);
			
			MethodSpec.Builder methodBuild = MethodSpec.methodBuilder(nameField);
			
			//See if its [], [[]] or nothing
			int contadorList = 0;
			if(field.getModifier() != null){
				if(field.getModifier().getClass().equals(List.class)) ++contadorList;
				if(field.getModifier().getCombinedWith() != null){
					for(Modificador m : field.getModifier().getCombinedWith()){
						if(m.getClass().equals(List.class)) ++contadorList;
					}
				}
			}
			
			boolean list = false;
			boolean nestedList = false;
			if(contadorList == 2) nestedList = true;
			else if(contadorList == 1) list = true;
			

			if(field.getClass().equals(ScalarField.class)){
				if(rangeField.equals("Int") || rangeField.equals("ID")) rangeField = "Integer";
				if(!list && !nestedList){
					// AAA : Int
					methodBuild.returns(className);
					if(!isInterface){
						methodBuild.addModifiers(Modifier.PUBLIC);
						methodBuild.addStatement("$T<String> result = connectVirtuoso(\"$L\") ", arrayList, field.getName());
		    			methodBuild.addStatement("if(result.size() == 0) return null");
		    			if(rangeField.equals("Integer")) methodBuild.addStatement("else return $L.parse$L(modifyScalarValue(result.get(0)))", rangeField, "Int");
		    			else if(rangeField.equals("String")) methodBuild.addStatement("else return modifyScalarValue(result.get(0))");
		    			else methodBuild.addStatement("else return $L.parse$L(modifyScalarValue(result.get(0)))", rangeField, rangeField);
					}else {
						methodBuild.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
					}
				}else if(list){
					//AAA : [Int]	
					methodBuild.returns(listOfClassName);
					if(!isInterface){
						methodBuild.addModifiers(Modifier.PUBLIC);
						methodBuild.addStatement("$T<String> $L = connectVirtuoso(\"$L\")", arrayList, nameField, field.getName());
						methodBuild.addStatement("$T<$L> $L = new ArrayList<>()", arrayList, rangeField, nameField+ "s");
						
						if(rangeField.equals("String")) methodBuild.addStatement("for(String value:$L) $L.add(modifyScalarValue(value))", nameField, nameField + "s");
				    	else if(rangeField.equals("Integer")) methodBuild.addStatement("for(String value:$L) $L.add($L.parse$L(modifyScalarValue(value)))", nameField, nameField + "s", rangeField, "Int");
				    	else methodBuild.addStatement("for(String value:$L) $L.add($L.parse$L(modifyScalarValue(value)))", nameField, nameField + "s", rangeField, rangeField);
						
						methodBuild.addStatement("if($L.size() == 0) return null", nameField + "s");
						methodBuild.addStatement("else return $L", nameField + "s");
					}else {
						methodBuild.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
					}
				}
			}else if(field.getClass().equals(ObjectField.class)){
				if(!interfaces.containsKey(field.getRange())){
					if(!list && !nestedList){
						// AAA : District
						methodBuild.returns(className);
						if(!isInterface){
							methodBuild.addModifiers(Modifier.PUBLIC);
							methodBuild.addStatement("$T<String> result = connectVirtuoso(\"$L\")", arrayList, field.getName());
			    			methodBuild.addStatement("if(result.size() == 0) return null");
				    		methodBuild.addStatement("else return new $L(result.get(0))", rangeField);
						}else {
							methodBuild.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
						}
					}else if(list){
						// AAA : [District]
						methodBuild.returns(listOfClassName);
						if(!isInterface){
							methodBuild.addModifiers(Modifier.PUBLIC);
							methodBuild.addStatement("$T<String> $L = connectVirtuoso(\"$L\")", arrayList, nameField, field.getName());
							methodBuild.addStatement("ArrayList<$L> $L = new ArrayList<>()", rangeField, nameField+ "s");
							methodBuild.addStatement("for(String id:$L) $L.add(new $L(id))", nameField, nameField + "s", rangeField);
							methodBuild.addStatement("if($L.size() == 0) return null", nameField + "s");
							methodBuild.addStatement("else return $L", nameField + "s");
						}else {
							methodBuild.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
						}
					}
				}else{
					if(!list && !nestedList){
						// AAA : Infrastructure
						methodBuild.returns(className);
						if(!isInterface){
							methodBuild.addModifiers(Modifier.PUBLIC);
							methodBuild.addStatement("$T<String> $L = connectVirtuoso(\"$L\")",  arrayList, nameField, field.getName());
							methodBuild.addCode("$L $L = null;\n", rangeField, "result");
							methodBuild.addCode("for(String id: $L){\n", nameField);
							
							boolean first = true;
							for(String possibleOption : interfaces.get(field.getRange())){
								index = possibleOption.lastIndexOf("/");
								String shortNamePossibleOption = possibleOption.substring(index + 1);
								if(first){methodBuild.addCode("\t if(connectVirtuoso(\"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\", id).contains(\"$L\"))  $L = new $L(id); \n",possibleOption , "result",shortNamePossibleOption ); first = false;}
								else methodBuild.addCode("\t else if(connectVirtuoso(\"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\", id).contains(\"$L\"))  $L = new $L(id); \n",possibleOption , "result",shortNamePossibleOption );
							}
							methodBuild.addCode("\t else $L.add(new $L(id)); \n" , nameField+ "s", rangeFieldNoModificado );
							
							methodBuild.addCode("\t else  $L = new $L(id); \n" , "result", rangeField );
							
							methodBuild.addCode("}\n");
							
							methodBuild.addStatement("if($L == null) return null", "result");
							methodBuild.addStatement("else return $L", "result");
						
						}else {
							methodBuild.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
						}
						
						
					}else if(list){
						// AA : [Infrastructure]
						methodBuild.returns(listOfClassName);
						if(!isInterface){
							methodBuild.addModifiers(Modifier.PUBLIC);
							methodBuild.addStatement("$T<String> $L = connectVirtuoso(\"$L\")",  arrayList, nameField, field.getName());
							methodBuild.addStatement("ArrayList<$L> $L = new ArrayList<>()", rangeField, nameField+ "s");
							methodBuild.addCode("for(String id: $L){\n", nameField);
							
							boolean first = true;
							for(String possibleOption : interfaces.get(field.getRange())){
								index = possibleOption.lastIndexOf("/");
								String shortNamePossibleOption = possibleOption.substring(index + 1);
								if(first){methodBuild.addCode("\t if(connectVirtuoso(\"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\", id).contains(\"$L\")) $L.add(new $L(id)); \n",possibleOption , nameField+ "s", shortNamePossibleOption ); first = false;}
								else methodBuild.addCode("\t else if(connectVirtuoso(\"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\", id).contains(\"$L\")) $L.add(new $L(id)); \n",possibleOption , nameField+ "s", shortNamePossibleOption );
							}
							methodBuild.addCode("\t else $L.add(new $L(id)); \n" , nameField+ "s", rangeFieldNoModificado );
							
							methodBuild.addCode("}\n");
							methodBuild.addStatement("if($L.size() == 0) return null", nameField + "s");
							methodBuild.addStatement("else return $L", nameField+ "s");
						}else {
							methodBuild.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
						}
						
					}
				}
			}
			
			MethodSpec method = methodBuild.build();
			methods.add(method);
			
		}
	}	
	static void createType(Object o, HashMap<String, ArrayList<String>> interfaces) throws IOException{
		
		ArrayList<MethodSpec> methods = new ArrayList<>();
		ArrayList<MethodSpec> methodsInterface = new ArrayList<>();
		Integer index = o.getName().lastIndexOf("/");
		String nameObject = o.getName().substring(index + 1);
		
		TypeSpec.Builder builderType = TypeSpec.classBuilder(nameObject)
		.addModifiers(Modifier.PUBLIC);
		
		//Crear interface + type
		if(interfaces.containsKey(o.getName())){ 
			builderType.addSuperinterface(ClassName.get("serverGraphQL", "I"+nameObject));
			
			MethodSpec method = MethodSpec.methodBuilder( nameObject + "Type")
				    .addModifiers(Modifier.PUBLIC)
				    .returns(ClassName.get("java.lang", "String"))
				    .addStatement("return $S", nameObject)
				    .build();
			methods.add(method);
			
			
			TypeSpec.Builder builderInterface = TypeSpec.interfaceBuilder("I"+nameObject)
			.addModifiers(Modifier.PUBLIC);
			
			
			// InfrastrustureType : String on interface
			method = MethodSpec.methodBuilder( nameObject + "Type")
				    .returns(ClassName.get("java.lang", "String"))
				    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
				    .build();
			methodsInterface.add(method);
			
			method = MethodSpec.methodBuilder( "idInstance")
					.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
				    .returns(ClassName.get("java.lang", "String"))
				    .build();
			methodsInterface.add(method);
			
			//Methods of interface
			addFields(o,interfaces, methodsInterface, true);
			
			
			for(MethodSpec m : methodsInterface) builderInterface.addMethod(m);
				
			
			TypeSpec InterfaceSpec = builderInterface.build();
			JavaFile javaFile = JavaFile.builder("serverGraphQL", InterfaceSpec)
				    .build();
	
			javaFile.writeTo(new File(fileDestination));	

		}
			
			builderType.addField(String.class, "idTurtle", Modifier.PRIVATE);
			
			//Constructor
			MethodSpec.Builder methodConstructor = MethodSpec.constructorBuilder()
				    .addModifiers(Modifier.PUBLIC)
				    .addStatement("this.$L = $L", "idTurtle", "idTurtle")
				    .addParameter(String.class, "idTurtle");
			
			methods.add(methodConstructor.build());
			
			
	
			
			// InfrastrustureType : String on subClasses
			for(String subClass : o.getSubClassOf()){
				
				index = subClass.lastIndexOf("/");
				String nameSubClass= subClass.substring(index + 1);
				
				System.out.println("sub " + o.getName() + " "+nameSubClass);
				
				builderType.addSuperinterface(ClassName.get("serverGraphQL", "I"+nameSubClass));

				
				MethodSpec method = MethodSpec.methodBuilder( nameSubClass + "Type")
					    .addModifiers(Modifier.PUBLIC)
					    .returns(ClassName.get("java.lang", "String"))
					    .addStatement("return $S", nameObject)
					    .build();
				methods.add(method);
			}
			
			MethodSpec method = MethodSpec.methodBuilder( "idInstance")
				    .addModifiers(Modifier.PUBLIC)
				    .returns(ClassName.get("java.lang", "String"))
				    .addStatement("return idTurtle")
				    .build();
			methods.add(method);
			
			addFields(o,interfaces, methods, false);
			
			for(MethodSpec m : methods) builderType.addMethod(m);
			
			builderType.addMethod(connectVirtuosoWithId());
			builderType.addMethod(connectVirtuoso());
			builderType.addMethod(createModifyScalarValue());
			
			TypeSpec typeSpec = builderType.build();
			JavaFile javaFile = JavaFile.builder("serverGraphQL", typeSpec)
				    .build();
	
			javaFile.writeTo(new File(fileDestination));	

	}
	
	public static MethodSpec constructorRepository(String nameObject, HashMap<String, ArrayList<String>> interfaces){
		Integer index = nameObject.lastIndexOf("/");
		String shortNameObject = nameObject.substring(index + 1);
		
		
		ClassName ArrayList = ClassName.get("java.util", "ArrayList");
		ClassName Query = ClassName.get("org.apache.jena.query", "Query");
		ClassName QueryFactory = ClassName.get("org.apache.jena.query", "QueryFactory");
		ClassName QuerySolution = ClassName.get("org.apache.jena.query", "QuerySolution");
		ClassName ResultSet = ClassName.get("org.apache.jena.query", "ResultSet");

		ClassName VirtGraph = ClassName.get("virtuoso.jena.driver", "VirtGraph");
		ClassName VirtuosoQueryExecution = ClassName.get("virtuoso.jena.driver", "VirtuosoQueryExecution");
		ClassName VirtuosoQueryExecutionFactory = ClassName.get("virtuoso.jena.driver", "VirtuosoQueryExecutionFactory");
		MethodSpec.Builder method = MethodSpec.constructorBuilder();
		if(!interfaces.containsKey(nameObject)){
					method.addCode("$L = new $T<>();\n", shortNameObject + "s", ArrayList)
					.addCode("$T graph = new $T (\"$L\", \"$L\", \"$L\");\n", VirtGraph , VirtGraph,  url_hostlist, user, password)
					.addCode("$T sparql = $T.create(\"Select ?subject FROM <$L> WHERE {\"\n", Query, QueryFactory, dbName)
					.addCode("+ \" ?subject <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <$L>.\"\n", nameObject)
					.addCode("+ \"}\");\n \n")
					.addCode("$T vqe = $T.create (sparql, graph);\n",VirtuosoQueryExecution, VirtuosoQueryExecutionFactory)
					.addCode("$T res = vqe.execSelect();\n", ResultSet)
					.addCode("while(res.hasNext()){\n")
					.addCode("\t $T qs = res.next();\n", QuerySolution)
					.addCode("\t String subject = qs.get(\"?subject\").toString();\n")
					.addCode("\t $L.add(new $L(subject));\n", shortNameObject + "s", shortNameObject)
					.addCode("}\n\n")
					.addCode("graph.close();\n")
				    .addModifiers(Modifier.PUBLIC);
		}else{

			method.addCode("$L = new $T<>();\n", shortNameObject + "s", ArrayList)
			.addCode("$T graph = new $T (\"$L\", \"$L\", \"$L\");\n", VirtGraph , VirtGraph,  url_hostlist, user, password)
			.addCode("$T sparql = $T.create(\"Select * FROM <$L> WHERE {\"\n", Query, QueryFactory  ,dbName);
			method.addCode("+ \" {?subject$L <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <$L>.}\"\n",shortNameObject, nameObject); 
			boolean first = true;
			for(String subclass : interfaces.get(nameObject)){
			  index = subclass.lastIndexOf("/");
			  String shortNameSubClass = subclass.substring(index + 1);
				  method.addCode("+ \" UNION \"\n"); 
				  method.addCode("+ \" {?subject$L <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <$L>.}\"\n", shortNameSubClass, subclass); 
			}
			

			method.addCode("+ \"}\" ); \n")
			.addCode("$T vqe = $T.create (sparql, graph);\n",VirtuosoQueryExecution, VirtuosoQueryExecutionFactory)
			.addCode("$T res = vqe.execSelect();\n", ResultSet)
			.addCode("while(res.hasNext()){\n")
			.addCode("\t $T qs = res.next();\n", QuerySolution)
			.addCode("\t String subject$L = null;\n", shortNameObject)
			.addCode("\t if(qs.get(\"?subject$L\") != null) subject$L = qs.get(\"?subject$L\").toString();\n", shortNameObject,  shortNameObject, shortNameObject);
			for(String subclass : interfaces.get(nameObject)){
				  index = subclass.lastIndexOf("/");
				  String shortNameSubClass = subclass.substring(index + 1);
				  method.addCode("\t String subject$L = null;\n", shortNameSubClass)
				  .addCode("\t if(qs.get(\"?subject$L\") != null) subject$L = qs.get(\"?subject$L\").toString();\n", shortNameSubClass,  shortNameSubClass, shortNameSubClass);
			}
			
			method.addCode("\t if(subject$L != null)$L.add(new $L(subject$L)); \n", shortNameObject, shortNameObject + "s",shortNameObject, shortNameObject);
			for(String subclass : interfaces.get(nameObject)){
				  index = subclass.lastIndexOf("/");
				  String shortNameSubClass = subclass.substring(index + 1);
			      method.addCode("\t else if(subject$L != null)$L.add(new $L(subject$L)); \n", shortNameSubClass,shortNameObject + "s",   shortNameSubClass, shortNameSubClass);
			}
			
			method.addCode("}\n\n")
			.addCode("graph.close();\n")
		    .addModifiers(Modifier.PUBLIC);

		}
		return method.build();

	}
	
	public static MethodSpec allInstancesRepository(String nameObject, boolean isInterface){
		ClassName clase;
		if(isInterface)  clase= getClassName("I"+nameObject);
		else clase = getClassName(nameObject);
		
		ClassName arrayList = ClassName.get("java.util", "List");
		
		TypeName listOfClass = ParameterizedTypeName.get(arrayList, clase);
		
		MethodSpec method = MethodSpec.methodBuilder("getAll" + nameObject + "s")
				.addStatement("return $L", nameObject + "s")
			    .addModifiers(Modifier.PUBLIC)
			    .returns(listOfClass)
			    .build();
		return method;

	}
	
	public static MethodSpec oneInstanceRepository(String nameObject,String shortNameObject, boolean isInterface, ArrayList<Object> createdObjects, HashMap<String, ArrayList<String>> interfaces ){
		ClassName clase;
		String returnType = shortNameObject;
		if(isInterface) { clase= getClassName("I"+shortNameObject); returnType = "I" + returnType;}
		else clase = getClassName(shortNameObject);
		
		ClassName ArrayList = ClassName.get("java.util", "ArrayList");
		ClassName Query = ClassName.get("org.apache.jena.query", "Query");
		ClassName QueryFactory = ClassName.get("org.apache.jena.query", "QueryFactory");
		ClassName QuerySolution = ClassName.get("org.apache.jena.query", "QuerySolution");
		ClassName ResultSet = ClassName.get("org.apache.jena.query", "ResultSet");

		ClassName VirtGraph = ClassName.get("virtuoso.jena.driver", "VirtGraph");
		ClassName VirtuosoQueryExecution = ClassName.get("virtuoso.jena.driver", "VirtuosoQueryExecution");
		ClassName VirtuosoQueryExecutionFactory = ClassName.get("virtuoso.jena.driver", "VirtuosoQueryExecutionFactory");
		
		//Identiffcar si ese ID es valido

		MethodSpec.Builder method = MethodSpec.methodBuilder("get" + shortNameObject)
		.addParameter(String.class, "id")
	    .addModifiers(Modifier.PUBLIC)
	    .returns(clase);
		
		
		method.addCode("$T graph = new $T (\"$L\", \"$L\", \"$L\");\n", VirtGraph , VirtGraph,  url_hostlist, user, password)
		.addCode("$T sparql = $T.create(\"Select * FROM <$L> WHERE {\"\n", Query, QueryFactory  ,dbName)
		.addCode("+ \" {<\"")
		.addCode("+ id ")
		.addCode("+ \"> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?tipo.}\"\n")
		.addCode("+ \"}\" ); \n")

		.addCode("$T vqe = $T.create (sparql, graph);\n",VirtuosoQueryExecution, VirtuosoQueryExecutionFactory)
		.addCode("$T res = vqe.execSelect();\n", ResultSet)
		.addCode("String tipo = null;")
		.addCode("while(res.hasNext()){\n")
		.addCode("\t $T qs = res.next();\n", QuerySolution)
		.addCode("\t if(qs.get(\"?tipo\") != null) tipo = qs.get(\"?tipo\").toString();\n")
		.addCode("}\n\n")
		.addCode("graph.close();\n")
		
		
		
		.addCode("$L result = null; \n", returnType)
		.addCode("if(tipo != null){\n");
		

		method.addCode("\t if(tipo.equals($S)) result =  new $L(id);\n", nameObject, shortNameObject);
		if(interfaces.containsKey(nameObject)){
			for(String subclass : interfaces.get(nameObject)){
				 Integer index = subclass.lastIndexOf("/");
				 String shortNameSubClass = subclass.substring(index + 1);
				method.addCode("\t else if(tipo.equals($S)) result = new $L(id);\n", subclass, shortNameSubClass);
			}
		}

		method.addCode("} else { \n")
		.addCode("\t result = null; \n")
		.addCode("} \n")
		.addCode("return result; \n");

		
		return method.build();

	}
	static void createRepository(Object o, HashMap<String, ArrayList<String>> interfaces, boolean isInterface, ArrayList<Object> createdObjects) throws IOException{
		
		Integer index = o.getName().lastIndexOf("/");
		String nameObject = o.getName().substring(index + 1);
		
		ClassName className;
		if(isInterface)  className = getClassName("I"+nameObject);
		else className = getClassName(nameObject);
		ClassName arrayList = ClassName.get("java.util", "ArrayList");
		TypeName listOfClassName = ParameterizedTypeName.get(arrayList, className);
		TypeName nestedListOfClassName = ParameterizedTypeName.get(arrayList, listOfClassName);
		
		
		TypeSpec.Builder builder = TypeSpec.classBuilder(nameObject + "Repository")
			    .addModifiers(Modifier.PUBLIC)
				.addField(listOfClassName, nameObject + "s", Modifier.PRIVATE, Modifier.FINAL);
		
		builder.addMethod(constructorRepository(o.getName(), interfaces));
		builder.addMethod(allInstancesRepository(nameObject, isInterface));
		builder.addMethod(oneInstanceRepository(o.getName(), nameObject, isInterface, createdObjects, interfaces));
		
		TypeSpec typeSpec = builder.build();
		
		JavaFile javaFile = JavaFile.builder("serverGraphQL", typeSpec)
			    .build();

		javaFile.writeTo(new File(fileDestination));	
	}
	
	public static MethodSpec queryList(String nameObject, boolean isInterface){
		
		ClassName clase;
		if(isInterface)  clase= getClassName("I"+nameObject);
		else clase = getClassName(nameObject);

		ClassName arrayList = ClassName.get("java.util", "List");
		
		TypeName listOfClass = ParameterizedTypeName.get(arrayList, clase);
		
		MethodSpec method = MethodSpec.methodBuilder("all" + nameObject + "s")
				.addCode("return $LRepositoryInstance.getAll$Ls();\n", nameObject, nameObject)
				.addModifiers(Modifier.PUBLIC)
				.returns(listOfClass)
				.build();
		return method;
	}
	
	public static MethodSpec queryOne(String nameObject, boolean isInterface){
		ClassName clase;
		if(isInterface)  clase= getClassName("I"+nameObject);
		else clase = getClassName(nameObject);


		MethodSpec method = MethodSpec.methodBuilder("get" + nameObject )
				.addCode("return $LRepositoryInstance.get$L(id);\n", nameObject, nameObject)
				.addModifiers(Modifier.PUBLIC)
				.returns(clase)
				.addParameter(String.class, "id")
				.build();
		return method;
	}
	
	
	public void buildQuery(ArrayList<String> repositories, HashMap<String, ArrayList<String>> interfaces) throws IOException{
		ClassName claseImplements = ClassName.get("com.coxautodev.graphql.tools", "GraphQLQueryResolver");
		ClassName arrayList = ClassName.get("java.util", "ArrayList");
		ClassName claseExtends = ClassName.get("graphql.servlet" , "SimpleGraphQLServlet");
		ClassName schemaParser = ClassName.get("com.coxautodev.graphql.tools" , "SchemaParser");
		ClassName GraphQLSchema = ClassName.get("graphql.schema" , "GraphQLSchema");
		ClassName webServlet = ClassName.get("javax.servlet.annotation" , "WebServlet");
		
		TypeName listOfClass = ParameterizedTypeName.get(arrayList, claseImplements);
		
		TypeSpec.Builder builderQuery = TypeSpec.classBuilder("Query")
			    .addModifiers(Modifier.PUBLIC)
			    .addSuperinterface(claseImplements);
		
		TypeSpec.Builder builderGraphQLEndPoint = TypeSpec.classBuilder("GraphQLEndPoint")
				.addModifiers(Modifier.PUBLIC)
				.superclass(claseExtends)
			    .addAnnotation(AnnotationSpec.builder(webServlet)
	                    .addMember("value", "$L", "urlPatterns = \"/graphql\"")
	                    .build());
		
		MethodSpec.Builder constructorQueryBuilder = MethodSpec.constructorBuilder()
			    .addModifiers(Modifier.PUBLIC);
		
		ClassLoader classLoader = getClass().getClassLoader();
		//File file= new File(classLoader.getResource("esquema.graphqls").getFile());
		File file= new File(getServletContext().getRealPath("WEB-INF/classes/esquema.graphqls"));
		
	    BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
	    try {

	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	    } finally {
	        br.close();
	    }
	    
		MethodSpec.Builder constructorGraphQLEndPoint = MethodSpec.constructorBuilder()
			    .addModifiers(Modifier.PUBLIC)
			    .addCode("super($T.newParser()\n", schemaParser)
			    .addCode(".schemaString($S)\n", sb.toString())
				.addCode(".resolvers(new $L(", "Query");
				//.addCode(".file($S)\n", "esquema.graphqls");
				

		
		
		boolean first = true;
		for(String repo : repositories){
			boolean isInterface = false;
			if(interfaces.containsKey(repo)) isInterface = true;
			
			Integer index = repo.lastIndexOf("/");
			repo = repo.substring(index + 1);
			
			builderQuery.addMethod(queryList(repo, isInterface));
			builderQuery.addMethod(queryOne(repo, isInterface));
			
			String repository = repo + "Repository";
			ClassName clase = ClassName.get("serverGraphQL", repository);
			String repoInstance = repository + "Instance";
			builderQuery.addField(clase, repoInstance , Modifier.PRIVATE, Modifier.FINAL); 
			constructorQueryBuilder.addParameter(clase, repoInstance);
			constructorQueryBuilder.addStatement("this.$N = $N", repoInstance , repoInstance );
			
			if(first) {constructorGraphQLEndPoint.addCode("new $L()", repository); first = false;}
			else constructorGraphQLEndPoint.addCode(", new $L()", repository);
		}
		
		constructorGraphQLEndPoint.addCode("))\n");
		
		for(String repo : repositories){
			Integer index = repo.lastIndexOf("/");
			repo = repo.substring(index + 1);
			constructorGraphQLEndPoint.addCode(".dictionary( "+ repo+".class)\n");
		}
		constructorGraphQLEndPoint.addCode(".build()\n");
		constructorGraphQLEndPoint.addCode(".makeExecutableSchema());");
		
		
		builderQuery.addMethod(constructorQueryBuilder.build());
		
		builderGraphQLEndPoint.addMethod(constructorGraphQLEndPoint.build());
		
		//Query
		System.out.println("build query" + fileDestination);
		TypeSpec typeSpec = builderQuery.build();
		JavaFile javaFile = JavaFile.builder("serverGraphQL", typeSpec)
			    .build();
		javaFile.writeTo(new File(fileDestination));	
		
		//GraphQLEndPoint
		typeSpec = builderGraphQLEndPoint.build();
		javaFile = JavaFile.builder("serverGraphQL", typeSpec)
			    .build();
		javaFile.writeTo(new File(fileDestination));	
		

	}

	
	public  void createServer(ArrayList<Object> createdObjects, HashMap<String, ArrayList<String>> interfaces) throws IOException{
		ArrayList<String> repositories = new ArrayList<>();
		for(Object o : createdObjects){
			createType(o,interfaces);
			createRepository(o, interfaces, o.isInterface(), createdObjects);
			repositories.add(o.getName());
		}
		buildQuery(repositories, interfaces);
	}
	/**
	 * Executes a SPARQL query against a virtuoso url and prints results.
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	public void main() throws IOException {
		
		ClassLoader classLoader = getClass().getClassLoader();

		System.out.println("herr");
		System.out.println(classLoader.getResource("."));
		System.out.println(classLoader.getResource(".").getFile().substring(1));
		System.out.println(classLoader.getResource("/"));
		System.out.println(classLoader.getResource("esquema.graphqls"));
		
		//fileDestination = getServletContext().getRealPath("WEB-INF/classes");
		//fileDestination = classLoader.getResource(".").getFile();
		fileDestination = getServletContext().getRealPath("WEB-INF/classes/");
		System.out.println(	fileDestination);

		
		File[] files = new File(getServletContext().getRealPath("WEB-INF/classes/") + "serverGraphQL").listFiles();
		if(files != null){ 
			for (File file : files) {
				 String path = getServletContext().getRealPath("WEB-INF/classes/") + "serverGraphQL/" + file.getName();
				 Path p = Paths.get(path) ;
				 Files.delete(p);
			 }
		}
		

		VirtGraph graph = new VirtGraph (url_hostlist, user, password);

		graph.clear ();

		ArrayList<Object> createdObjects = new ArrayList<>();
		HashMap<String, ArrayList<String>> interfaces = new HashMap<>();
		ArrayList<Field> createdField= new ArrayList<>();
		
		//get Objects
		
		getFields(createdField, graph);
		getObjects(createdObjects, createdField, interfaces, graph);

		graph.close();
		
		for(Object o : createdObjects){
			System.out.println(o.getName().toString());
			
		}

		File file= new File(getServletContext().getRealPath("WEB-INF/classes/esquema.graphqls"));
		if(!file.exists()) file.createNewFile();
		
		//file= new File(classLoader.getResource("esquema.graphqls").getFile());
		//file= new File(getServletContext().getRealPath("WEB-INF/classes/esquema.graphqls"));
		FileWriter fw = new FileWriter(file);

		
        for(Object o : createdObjects){
        	boolean interfaz = false;
    		Integer index = o.getName().lastIndexOf("/");
			String shortName = o.getName().substring(index + 1);
			
			//Is a superclass, so we have to create a type and a interface
        	if(interfaces.containsKey(o.getName())){ 
        		o.setInterface(true);
    			fw.write("interface I" + shortName + " {" + "\r\n"); //First line
    			fw.write("	" + shortName + "Type: String!" + "\r\n"); //type 
    			fw.write("	" + "idInstance : String!" + "\r\n"); //type 
    			writeFields(o, fw, interfaces);
    			fw.write("}" + "\r\n" + "\r\n"); //End interface
    			
    			fw.write("type " + shortName + " implements I" + shortName + " {" + "\r\n");
    			fw.write("	" + shortName + "Type: String!" + "\r\n"); //type 
    			
    			writeFields(o, fw, interfaces);
    			fw.write("	" + "idInstance : String!" + "\r\n"); //type 
    			fw.write("}" + "\r\n" + "\r\n"); //End interface
        	}else{

        		fw.write("type " + shortName + " ");
        		if(o.getSubClassOf().size() == 0)  fw.write("{" + "\r\n");
        		else{
	        		int i = 0;
	        		for(String subClassOf : o.getSubClassOf()){
	        			index = subClassOf.lastIndexOf("/");
	        			String shortNameSubClass = subClassOf.substring(index + 1);
	        			if(i == 0) fw.write("implements I" + shortNameSubClass);
	        			else fw.write(", I" + shortNameSubClass);
	        			++i;
	        		}
	        		fw.write("{" + "\r\n");
        			
    				//write Field of interface in the type
        			for(String subClassOf : o.getSubClassOf()){
        				for(Object searchParent : createdObjects){
        					if(searchParent.getName().equals(subClassOf)){
        						//add Fields of parents to object
        						o.getFields().addAll(searchParent.getFields());
        						//writeFields(searchParent, fw,  interfaces);
        	        			index = subClassOf.lastIndexOf("/");
        	        			String shortNameSubClass = subClassOf.substring(index + 1);
        	        			fw.write("	" + shortNameSubClass + "Type: String!" + "\r\n"); //type 
        					}
        				}
        			}
        		}
        		writeFields(o, fw,  interfaces);
        		fw.write("	" + "idInstance : String!" + "\r\n"); //type 
        		fw.write("}" + "\r\n" + "\r\n"); //End type/ interface
        	}
        }
        


        
        //Queries
        fw.write("type Query {" + "\r\n");
        for(int i = 0; i < createdObjects.size(); ++i){
        	
        	Integer index = createdObjects.get(i).getName().lastIndexOf("/");
			String shortNameSubClass = createdObjects.get(i).getName().substring(index + 1);
			
			String rangeField = shortNameSubClass;
			if(interfaces.containsKey(createdObjects.get(i).getName()))  rangeField = "I"+rangeField;
        	fw.write("	" + "all"+ shortNameSubClass + "s: [" + rangeField +"]" + "\r\n");
        	fw.write("	" + "get"+ shortNameSubClass + "(id: String!): " + rangeField +"" + "\r\n");
        		
        }
        fw.write("}" + "\r\n");
        
        fw.write("schema {" + "\r\n");
        fw.write("	query: Query" + "\r\n");
        fw.write("}" + "\r\n" + "\r\n");
        fw.close();

        createServer(createdObjects, interfaces);

        String command = "javac -cp \"../../lib/*\" *.java";
        Runtime.getRuntime().exec(command,null, new File(getServletContext().getRealPath("WEB-INF/classes/") + "serverGraphQL"));

	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		url_hostlist = request.getParameter("Url_Virtuoso");
		user = request.getParameter("Usuari");
		password = request.getParameter("Password");
		dbName = request.getParameter("DbName");
		
		
		System.out.println(request.getServletContext().getContextPath().toString());
		
		System.out.println( "entro en post" +  getServletContext().getRealPath("WEB-INF/classes"));
		
		//destinationPathApiGraphQL = getServletContext().getRealPath("WEB-INF/resources/esquema.graphqls");
		//fileDestination = getServletContext().getRealPath("WEB-INF/classes");
		//System.out.println(fileDestination);
		//System.out.println("aaa" + getClass().getClassLoader().getResourceAsStream("test.arff").toString());
		/*
		File f = new File(destinationPathApiGraphQL);
		System.out.println(destinationPathApiGraphQL);
		f.createNewFile();

		//URL location = getClass().getProtectionDomain().getCodeSource().getLocation();
		System.out.println(destinationPathApiGraphQL);

        System.out.println("----");
        */
        //System.out.println(this.getClass().getResource("esquema.jsp").getPath());
		main();
		
        PrintWriter writer = response.getWriter();

        
		if(!url_hostlist.isEmpty() && !user.isEmpty() && !password.isEmpty() && !dbName.isEmpty()){
	        writer.println("<html>Correcto</html>");
		}else{
	        writer.println("<html>");
	        writer.println("Omple tots els camps <br>");
	        writer.println("<a href=\"index.jsp\"> Enrrere </a> <br>");
	        writer.println("</html>");
	        
		}
        writer.flush();
		
		
	}
	

	


}
