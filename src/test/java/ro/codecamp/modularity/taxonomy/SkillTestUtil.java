package ro.codecamp.modularity.taxonomy;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import ro.codecamp.modularity.taxonomy.control.SkillStore;
import ro.codecamp.modularity.taxonomy.entity.SkillCategory;

@Path("/testdata/skills")
public class SkillTestUtil {

	@Inject
	private SkillStore skillStore;
	
	@POST
	public void createTaxonomy() {
		SkillCategory root = skillStore.persist(new SkillCategory("Taxonomy",
				null));
		skillStore.persist(createDevelopment(root));
		skillStore.persist(createCertifications(root));
		skillStore.persist(createSoftwareArchitecture(root));
	}

	private SkillCategory createDevelopment(SkillCategory root) {
		SkillCategory development = new SkillCategory("Development", root);

		SkillCategory tools = new SkillCategory("Building Tools", development);
		SkillCategory buildTools = new SkillCategory("Building Tools", tools);
		buildTools.getSubCategories().add(
				new SkillCategory("Apache Ant", buildTools));
		buildTools.getSubCategories().add(
				new SkillCategory("Apache Maven", buildTools));
		buildTools.getSubCategories().add(
				new SkillCategory("CPPAnt", buildTools));
		buildTools.getSubCategories()
				.add(new SkillCategory("NAnt", buildTools));
		buildTools.getSubCategories()
				.add(new SkillCategory("Rake", buildTools));
		buildTools.getSubCategories().add(
				new SkillCategory("Gradle", buildTools));
		buildTools.getSubCategories().add(
				new SkillCategory("MSBuild", buildTools));
		buildTools.getSubCategories().add(
				new SkillCategory("Grunt.js", buildTools));
		tools.getSubCategories().add(buildTools);
		SkillCategory contInt = new SkillCategory(
				"Continuous Integration Tools", tools);
		contInt.getSubCategories().add(new SkillCategory("Anthill", contInt));
		contInt.getSubCategories().add(new SkillCategory("Bamboo", contInt));
		contInt.getSubCategories().add(new SkillCategory("BuiltIT", contInt));
		contInt.getSubCategories().add(
				new SkillCategory("CruiseControl", contInt));
		contInt.getSubCategories().add(new SkillCategory("Jenkins", contInt));
		contInt.getSubCategories().add(
				new SkillCategory("Team Foundation Server", contInt));
		tools.getSubCategories().add(contInt);
		SkillCategory contMng = new SkillCategory(
				"Configuration Management Tools", tools);
		contMng.getSubCategories().add(new SkillCategory("Ansible", contMng));
		contMng.getSubCategories().add(new SkillCategory("Chef", contMng));
		contMng.getSubCategories().add(new SkillCategory("Puppet", contMng));
		contMng.getSubCategories().add(new SkillCategory("Salt", contMng));
		tools.getSubCategories().add(contMng);
		SkillCategory versionControl = new SkillCategory(
				"Version Control Systems", tools);
		versionControl.getSubCategories().add(
				new SkillCategory("CVS", versionControl));
		versionControl.getSubCategories().add(
				new SkillCategory("Microsoft VSS", versionControl));
		versionControl.getSubCategories().add(
				new SkillCategory("Rational ClearCase", versionControl));
		versionControl.getSubCategories().add(
				new SkillCategory("Subversion", versionControl));
		versionControl.getSubCategories().add(
				new SkillCategory("TFS", versionControl));
		tools.getSubCategories().add(versionControl);

		development.getSubCategories().add(tools);

		SkillCategory lang = new SkillCategory("Languages and Frameworks",
				development);

		SkillCategory netTech = new SkillCategory(
				".NET Technologies / Frameworks", lang);
		SkillCategory net = new SkillCategory(".Net", netTech);
		net.getSubCategories()
				.add(new SkillCategory(".Net Framework 2.0", net));
		net.getSubCategories()
				.add(new SkillCategory(".Net Framework 3.0", net));
		net.getSubCategories()
				.add(new SkillCategory(".Net Framework 3.5", net));
		net.getSubCategories()
				.add(new SkillCategory(".Net Framework 4.0", net));
		net.getSubCategories().add(
				new SkillCategory(".Net Framework Generic", net));
		net.getSubCategories().add(new SkillCategory(".Net 4", net));
		netTech.getSubCategories().add(net);
		SkillCategory aspNet = new SkillCategory("ASP.NET", netTech);
		aspNet.getSubCategories().add(new SkillCategory("ASP.NET MVC", aspNet));
		aspNet.getSubCategories().add(
				new SkillCategory("ASP.NET MVC Razor 4", aspNet));
		aspNet.getSubCategories().add(
				new SkillCategory("ASP.NET MVC 4.0", aspNet));
		aspNet.getSubCategories().add(new SkillCategory("ASP.NET 2.0", aspNet));
		aspNet.getSubCategories().add(new SkillCategory("ASP.NET 3.0", aspNet));
		aspNet.getSubCategories().add(new SkillCategory("ASP.NET 3.5", aspNet));
		aspNet.getSubCategories().add(new SkillCategory("ASP.NET 4.0", aspNet));
		aspNet.getSubCategories().add(
				new SkillCategory("ASP.NET Generic", aspNet));
		netTech.getSubCategories().add(aspNet);

		netTech.getSubCategories().add(new SkillCategory("LINQ2SQL", netTech));
		netTech.getSubCategories().add(
				new SkillCategory(".Net Remoting", netTech));
		netTech.getSubCategories().add(new SkillCategory("ADO .Net", netTech));
		netTech.getSubCategories().add(new SkillCategory("Azure", netTech));
		netTech.getSubCategories().add(new SkillCategory("CAB UI", netTech));
		netTech.getSubCategories()
				.add(new SkillCategory("Nhibernate", netTech));
		netTech.getSubCategories()
				.add(new SkillCategory("Spring.NET", netTech));
		SkillCategory wcf = new SkillCategory("WCF", netTech);
		wcf.getSubCategories().add(new SkillCategory("REST WCF", wcf));
		netTech.getSubCategories().add(wcf);

		lang.getSubCategories().add(netTech);

		SkillCategory javaTech = new SkillCategory(
				"Java Technologies / Frameworks", lang);
		javaTech.getSubCategories().add(new SkillCategory("EJB", javaTech));
		javaTech.getSubCategories().add(new SkillCategory("Grails", javaTech));
		javaTech.getSubCategories().add(
				new SkillCategory("Hibernate", javaTech));
		javaTech.getSubCategories().add(new SkillCategory("JAAS", javaTech));
		SkillCategory jws = new SkillCategory("Java Web Services", javaTech);
		jws.getSubCategories().add(new SkillCategory("Apache Axis", jws));
		jws.getSubCategories().add(new SkillCategory("Apache CXF", jws));
		javaTech.getSubCategories().add(jws);
		javaTech.getSubCategories().add(new SkillCategory("JPA", javaTech));
		javaTech.getSubCategories().add(new SkillCategory("JSF", javaTech));
		javaTech.getSubCategories().add(new SkillCategory("JSP", javaTech));
		javaTech.getSubCategories().add(new SkillCategory("JMS", javaTech));
		javaTech.getSubCategories().add(new SkillCategory("JUnit", javaTech));

		lang.getSubCategories().add(javaTech);

		SkillCategory jsTech = new SkillCategory(
				"JavaScript Technologies / Frameworks", lang);
		jsTech.getSubCategories().add(new SkillCategory("jQuery", jsTech));
		jsTech.getSubCategories().add(new SkillCategory("node.js", jsTech));
		jsTech.getSubCategories().add(new SkillCategory("AngularJS", jsTech));
		jsTech.getSubCategories().add(new SkillCategory("RequireJS", jsTech));

		lang.getSubCategories().add(jsTech);

		development.getSubCategories().add(lang);

		return development;
	}

	private SkillCategory createCertifications(SkillCategory root) {
		SkillCategory certifications = new SkillCategory("Certifications", root);

		SkillCategory dev = new SkillCategory("Development", certifications);
		certifications.getSubCategories().add(dev);

		SkillCategory oracle = new SkillCategory("Oracle", dev);
		dev.getSubCategories().add(oracle);
		SkillCategory javase = new SkillCategory("Java SE", oracle);
		oracle.getSubCategories().add(javase);
		javase.getSubCategories().add(
				new SkillCategory(
						"Oracle Certified Professional, Java SE 6 Programmer",
						javase));
		javase.getSubCategories()
				.add(new SkillCategory(
						"Oracle Certified Master, Java SE 6 Developer", javase));
		SkillCategory javaee = new SkillCategory("Java EE", oracle);
		oracle.getSubCategories().add(javaee);
		javaee.getSubCategories()
				.add(new SkillCategory(
						"Oracle Certified Expert, Java EE 6 Enterprise JavaBeans Developer",
						javaee));
		javaee.getSubCategories()
				.add(new SkillCategory(
						"Oracle Certified Expert, Java EE 6 Java Persistence API Developer",
						javaee));
		javaee.getSubCategories()
				.add(new SkillCategory(
						"Oracle Certified Expert, Java EE 6 Web Services Developer",
						javaee));
		javaee.getSubCategories()
				.add(new SkillCategory(
						"Oracle Certified Master, Java EE 6 Enterprise Architect",
						javaee));

		SkillCategory microsoft = new SkillCategory("Microsoft", dev);
		dev.getSubCategories().add(microsoft);
		SkillCategory net = new SkillCategory(".NET", microsoft);
		net.getSubCategories()
				.add(new SkillCategory(
						"MCTS: Microsoft .NET Framework 4, Windows Applications Development",
						net));
		net.getSubCategories()
				.add(new SkillCategory(
						"MCTS: Microsoft .NET Framework 4, Web Applications Development",
						net));
		net.getSubCategories()
				.add(new SkillCategory(
						"MCTS: Microsoft .NET Framework 4, Windows Communication Foundation Development",
						net));
		net.getSubCategories().add(
				new SkillCategory(
						"MCTS: Microsoft .NET Framework 4, Data Access", net));

		SkillCategory aa = new SkillCategory("Analysis & Architecture",
				certifications);
		certifications.getSubCategories().add(aa);
		SkillCategory mic = new SkillCategory("Microsoft", aa);
		aa.getSubCategories().add(mic);
		mic.getSubCategories().add(
				new SkillCategory(
						"Foundation Certificate in Business Analysis", mic));

		return certifications;
	}

	private SkillCategory createSoftwareArchitecture(SkillCategory root) {
		SkillCategory architecture = new SkillCategory(
				"Enterprise Architecture", null);
		SkillCategory sa = new SkillCategory("Software Architecture",
				architecture);
		architecture.getSubCategories().add(sa);
		SkillCategory ia = new SkillCategory("Infrastructure Architecture",
				architecture);
		architecture.getSubCategories().add(ia);

		return architecture;

	}

}
