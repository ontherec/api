package kr.ontherec.api;


import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(packages = "kr.ontherec.api", importOptions = DoNotIncludeTests.class)
public class PackageDependencyTest {

	private static final String INFRA = "..infra..";
	private static final String MODULES = "..modules..";
	private static final String SECURITY = "..socketio..";

	@ArchTest
	ArchRule socketioPackageRule = classes().that().resideInAPackage(SECURITY)
			.should().onlyHaveDependentClassesThat().resideInAnyPackage(SECURITY);

	@ArchTest
	ArchRule modulesPackageRule = classes().that().resideInAPackage(MODULES)
			.should().onlyHaveDependentClassesThat().resideInAnyPackage(SECURITY, MODULES);

	@ArchTest
	ArchRule infraPackageRule = classes().that().resideInAPackage(INFRA)
			.should().onlyHaveDependentClassesThat().resideInAnyPackage(INFRA, MODULES, SECURITY)
			.andShould().onlyDependOnClassesThat(
					resideInAnyPackage(INFRA).or(DescribedPredicate.not(resideInAnyPackage("kr.ontherec.api")))
			);

	@ArchTest
	ArchRule cycleCheck = slices().matching("kr.ontherec.api.(*)..")
			.should().beFreeOfCycles();

}
