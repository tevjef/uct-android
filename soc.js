
// dojo.require
dojo.require("dijit.Dialog");
dojo.require("dijit.Tooltip");
dojo.require("dijit.form.Button");
dojo.require("dijit.form.FilteringSelect");
dojo.require("dijit.form.TextBox");
dojo.require("dojo.data.ItemFileReadStore");
dojo.require("dojox.widget.Toaster");
dojo.require("dojo.back");

Object.size = function(obj) {
    var size = 0, key;
    for (key in obj) {
        if (obj.hasOwnProperty(key)) size++;
    }
    return size;
};

// dojo.addOnLoad
dojo.addOnLoad(function() {
	// add ie7 css override
	if (dojo.isIE == 7) dojo.attr("csslink", "href", "css/soc.ie7.css");
	// check to remove header/footer
	IframeUtils.handleIframe();
	// add browser detection
	BrowserUtils.detectVersion();
	// init form
	FormService.initPage();
	// load fragment url
	var fragment = HistoryUtils.getFragment();
	HistoryUtils.initHistory(fragment);
	ContentUtils.loadContent(fragment);
	// unhide page content
	dojo.byId("bodyTag").style["display"] = "";
});

// ContentUtils
var ContentUtils = (function() {
	// loadCoreCode
	var loadCoreCode = function(fragment) {
		// parse code, semester, campus, level
		if (1) {
			// code
			var code = UrlParserUtils.parse(fragment, "code");
			Constants.selectedCoreCode = code;
			// semester
			var semester = UrlParserUtils.parse(fragment, "semester");
			Constants.selectedSemester = semester;
			// campus
			var campus = UrlParserUtils.parse(fragment, "campus");
			Constants.selectedCampus = campus;
			// level
			var level = UrlParserUtils.parse(fragment, "level");
			Constants.selectedLevel = level;
			// searchType
			Constants.searchType = Constants.CORE_CODE_SEARCH;
			Constants.currentCoreCode = code;
		}
		// update house icon based on semester
		FormService.updateHouseIcon();
		// get subjects
		CourseService.initSubjects();
		// hide form, disclaimers, old version of soc link
		dojo.addClass("termlocationlevelform", "hidden");
		dojo.addClass("disclaimers", "hidden");
		// unhide search/filters content
		dojo.removeClass("searchfilterscontent", "hidden");
		// init search tabs
		SearchTabUtils.initLocationSearchTab();
		// update semester title
		SemesterUtils.updateSemesterTitle();
		// add tooltips for campus list (search title)
		FormService.createCampusTooltipsInTitle();
		// unhide level of study filters based on user selection
		FilterService.initLevelOfStudyFilters();
		// unhide new brunswick sub-campus filters based on user selection
		FilterService.initNewBrunswickSubCampusFilters();
		// disable all filters initially
		FilterUtils.disableFilters();
		// click on core code tab
		dojo.byId("core_code_search_id").click();
		// publish courses loading message
		CourseLoadingUtils.publishSearching();
		dojo.xhrGet({ url:"coreCodeCourses.json", content:{ semester:Constants.selectedSemester, campus:Constants.selectedCampus, level:Constants.selectedLevel, coreCode:Constants.currentCoreCode }, handleAs:"json", error:Constants.ERROR,
			load:function(json) {
				Constants.courses = json;
				CoreCodeCourseUtils.loadCourses(json);
			}
		});
	};
	// loadUnitCourse
	var loadUnitCourse = function(fragment) {
		// parse semester, campus, unit, subject, course number
		if (1) {
			// semester
			var semester = UrlParserUtils.parse(fragment, "semester");
			Constants.selectedSemester = semester;
			// campus
			var campus = UrlParserUtils.parse(fragment, "campus");
			Constants.selectedCampus = campus;
			// unit
			var unit = UrlParserUtils.parse(fragment, "unit");
			Constants.selectedUnit = unit;
			// subject
			var subject = UrlParserUtils.parse(fragment, "subject");
			Constants.selectedSubject = subject;
			// course number
			var courseNumber = UrlParserUtils.parse(fragment, "courseNumber");
			Constants.selectedCourseNumber = courseNumber;
			// level
			Constants.selectedLevel = (courseNumber < 500 ? "U" : "G");
			// searchType
			Constants.searchType = Constants.UNIT_COURSE_SEARCH;
		}
		// update house icon based on semester
		FormService.updateHouseIcon();
		// get subjects
		CourseService.initSubjects();
		// hide form, disclaimers, old version of soc link
		dojo.addClass("termlocationlevelform", "hidden");
		dojo.addClass("disclaimers", "hidden");
		// unhide search/filters content
		dojo.removeClass("searchfilterscontent", "hidden");
		// init search tabs
		SearchTabUtils.initLocationSearchTab();
		// update semester title
		SemesterUtils.updateSemesterTitle();
		// add tooltips for campus list (search title)
		FormService.createCampusTooltipsInTitle();
		// unhide level of study filters based on user selection
		FilterService.initLevelOfStudyFilters();
		// unhide new brunswick sub-campus filters based on user selection
		FilterService.initNewBrunswickSubCampusFilters();
		// hide course types filters if online
		FilterService.initCourseTypesFilters();
		// hide day & time filters if online
		FilterService.initDayAndTimeFilters();
		// disable all filters initially
		FilterUtils.disableFilters();
		// publish courses loading message
		CourseLoadingUtils.publishSearching();
		// get courses
		dojo.xhrGet({ url:"campusUnitCourse.json", content:{ semester:Constants.selectedSemester, campus:Constants.selectedCampus, unit:Constants.selectedUnit, subject:Constants.selectedSubject, courseNumber:Constants.selectedCourseNumber }, handleAs:"json", error:Constants.ERROR,
			load:function(json) {
				Constants.courses = json;
				SingleSubjectCourseUtils.refreshCourses(Constants.selectedSubject, json);
			}
		});
	};
	// loadKeyword
	var loadKeyword = function(fragment) {
		console.log("loading keyword: " + fragment);
		// parse subject, semester, campus, level
		if (1) {
			// keyword
			var keyword = UrlParserUtils.parse(fragment, "keyword");
			Constants.currentKeyword = keyword;
			// semester
			var semester = UrlParserUtils.parse(fragment, "semester");
			Constants.selectedSemester = semester;
			// campus
			var campus = UrlParserUtils.parse(fragment, "campus");
			Constants.selectedCampus = campus;
			// level
			var level = UrlParserUtils.parse(fragment, "level");
			Constants.selectedLevel = level;
			// searchType
			Constants.searchType = Constants.LOCATION_SEARCH;
		}
		// update house icon based on semester
		FormService.updateHouseIcon();
		// get subjects
		CourseService.initSubjects();
		// hide form, disclaimers, old version of soc link
		dojo.addClass("termlocationlevelform", "hidden");
		dojo.addClass("disclaimers", "hidden");
		// unhide search/filters content
		dojo.removeClass("searchfilterscontent", "hidden");
		// init search tabs
		SearchTabUtils.initLocationSearchTab();
		// update semester title
		SemesterUtils.updateSemesterTitle();
		// add tooltips for campus list (search title)
		FormService.createCampusTooltipsInTitle();
		// unhide level of study filters based on user selection
		FilterService.initLevelOfStudyFilters();
		// unhide new brunswick sub-campus filters based on user selection
		FilterService.initNewBrunswickSubCampusFilters();
		// hide course types filters if online
		FilterService.initCourseTypesFilters();
		// hide day & time filters if online
		FilterService.initDayAndTimeFilters();
		// disable all filters initially
		FilterUtils.disableFilters();
		// click on keyword tab, set input value
		dojo.byId("keyword_search_id").click();
		dojo.byId("keyword_textbox_id").value = Constants.currentKeyword;
		// publish courses loading message
		CourseLoadingUtils.publishSearching();
		// get courses
		if (Constants.isOnlineCourses()) {
			dojo.xhrGet({ url:"onlineKeyword.json", content:{ year:Constants.getSelectedYear(), term:Constants.getSelectedTerm(), level:Constants.selectedLevel, keyword:keyword }, handleAs:"json", error:Constants.ERROR,
				load:function(json) {
					Constants.courses = json;
					KeywordCourseUtils.loadCourses(json);
				}
			});
		}
		else {
			dojo.xhrGet({ url:"keyword.json", content:{ semester:Constants.selectedSemester, campus:Constants.selectedCampus, level:Constants.selectedLevel, keyword:keyword }, handleAs:"json", error:Constants.ERROR,
				load:function(json) {
					Constants.courses = json;
					KeywordCourseUtils.loadCourses(json);
				}
			});
		}
	};
	// loadMulti
	var loadMulti = function(fragment) {
		console.log("loading multi: " + fragment);
		// parse subject, semester, campus, level
		if (1) {
			// subject
			var subject = UrlParserUtils.parse(fragment, "subject");
			Constants.selectedSubject = subject;
			// semester
			var semester = UrlParserUtils.parse(fragment, "semester");
			Constants.selectedSemester = semester;
			// campus
			var campus = UrlParserUtils.parse(fragment, "campus");
			Constants.selectedCampus = campus;
			// level
			var level = UrlParserUtils.parse(fragment, "level");
			Constants.selectedLevel = level;
			// searchType
			Constants.searchType = Constants.LOCATION_SEARCH;
		}
		// update house icon based on semester
		FormService.updateHouseIcon();
		// get subjects
		CourseService.initSubjects();
		// hide form, disclaimers, old version of soc link
		dojo.addClass("termlocationlevelform", "hidden");
		dojo.addClass("disclaimers", "hidden");
		// unhide search/filters content
		dojo.removeClass("searchfilterscontent", "hidden");
		// init search tabs
		SearchTabUtils.initLocationSearchTab();
		// update semester title
		SemesterUtils.updateSemesterTitle();
		// add tooltips for campus list (search title)
		FormService.createCampusTooltipsInTitle();
		// unhide level of study filters based on user selection
		FilterService.initLevelOfStudyFilters();
		// unhide new brunswick sub-campus filters based on user selection
		FilterService.initNewBrunswickSubCampusFilters();
		// hide course types filters if online
		FilterService.initCourseTypesFilters();
		// hide day & time filters if online
		FilterService.initDayAndTimeFilters();
		// disable all filters initially
		FilterUtils.disableFilters();
		// publish courses loading message
		CourseLoadingUtils.publishSearching();
		// get courses
		if (Constants.isOnlineCourses()) {
			dojo.xhrGet({ url:"onlineMulti.json", content:{ subjects:Constants.selectedSubject, year:Constants.getSelectedYear(), term:Constants.getSelectedTerm(), level:Constants.selectedLevel }, handleAs:"json", error:Constants.ERROR,
				load:function(json) {
					Constants.courses = json;
					MultiSubjectCourseUtils.loadCourses(json);
				}
			});
		}
		else {
			dojo.xhrGet({ url:"multi.json", content:{ subjects:Constants.selectedSubject, semester:Constants.selectedSemester, campus:Constants.selectedCampus, level:Constants.selectedLevel }, handleAs:"json", error:Constants.ERROR,
				load:function(json) {
					Constants.courses = json;
					MultiSubjectCourseUtils.loadCourses(json);
				}
			});
		}
	};
	// loadCourses
	var loadCourses = function(fragment) {
		console.log("loading courses: " + fragment);
		// parse subject, semester, campus, level
		if (1) {
			// subject
			var subject = UrlParserUtils.parse(fragment, "subject");
			Constants.selectedSubject = subject;
			// semester
			var semester = UrlParserUtils.parse(fragment, "semester");
			Constants.selectedSemester = semester;
			// campus
			var campus = UrlParserUtils.parse(fragment, "campus");
			Constants.selectedCampus = campus;
			// level
			var level = UrlParserUtils.parse(fragment, "level");
			Constants.selectedLevel = level;
			// searchType
			Constants.searchType = Constants.LOCATION_SEARCH;
		}
		// update house icon based on semester
		FormService.updateHouseIcon();
		// get subjects
		CourseService.initSubjects();
		// hide form, disclaimers, old version of soc link
		dojo.addClass("termlocationlevelform", "hidden");
		dojo.addClass("disclaimers", "hidden");
		// unhide search/filters content
		dojo.removeClass("searchfilterscontent", "hidden");
		// init search tabs
		SearchTabUtils.initLocationSearchTab();
		// update semester title
		SemesterUtils.updateSemesterTitle();
		// add tooltips for campus list (search title)
		FormService.createCampusTooltipsInTitle();
		// unhide level of study filters based on user selection
		FilterService.initLevelOfStudyFilters();
		// unhide new brunswick sub-campus filters based on user selection
		FilterService.initNewBrunswickSubCampusFilters();
		// hide course types filters if online
		FilterService.initCourseTypesFilters();
		// hide day & time filters if online
		FilterService.initDayAndTimeFilters();
		// disable all filters initially
		FilterUtils.disableFilters();
		// publish courses loading message
		CourseLoadingUtils.publishSearching();
		// get courses
		if (Constants.isOnlineCourses()) {
			dojo.xhrGet({ url:"onlineCourses.json", content:{ subject:Constants.selectedSubject, year:Constants.getSelectedYear(), term:Constants.getSelectedTerm(), level:Constants.selectedLevel }, handleAs:"json", error:Constants.ERROR,
				load:function(json) {
					Constants.courses = json;
					SingleSubjectCourseUtils.refreshCourses(Constants.selectedSubject, json);
				}
			});
		}
		else {
			dojo.xhrGet({ url:"courses.json", content:{ subject:Constants.selectedSubject, semester:Constants.selectedSemester, campus:Constants.selectedCampus, level:Constants.selectedLevel }, handleAs:"json", error:Constants.ERROR,
				load:function(json) {
					Constants.courses = json;
					SingleSubjectCourseUtils.refreshCourses(Constants.selectedSubject, json);
				}
			});
		}
	};
	// loadSchools
	var loadSchools = function(fragment) {
		console.log("loading schools: " + fragment);
		// parse semester school
		if (1) {
			// semester
			var semester = UrlParserUtils.parse(fragment, "semester");
			Constants.selectedSemester = semester;
			// school
			var school = UrlParserUtils.parse(fragment, "school");
			Constants.selectedSchool = school;
			// searchType
			Constants.searchType = Constants.SCHOOL_SEARCH;
		}
		// reset
		FormService.reset();
		SearchUtils.searchBySchool();
		// update house icon based on semester
		FormService.updateHouseIcon();
		// init schools dropdown
		SchoolSearchUtils.initSchoolFilteringSelectWidget();
		// hide form, disclaimers, old version of soc link
		dojo.addClass("termlocationlevelform", "hidden");
		dojo.addClass("disclaimers", "hidden");
		MessageUtils.hideMessages();
		// unhide search/filters content
		dojo.removeClass("searchfilterscontent", "hidden");
		// init search tabs
		SearchTabUtils.initSchoolSearchTab();
		// update semester title
		SemesterUtils.updateSemesterTitle();
		// unhide level of study filters based on user selection
		FilterService.initLevelOfStudyFilters();
		// disable all filters initially
		FilterUtils.disableFilters();
		// hide instructions
		InstructionUtils.hideInstructions();
		// disable all filters initially
		FilterUtils.disableFilters();
		// publish courses loading message
		CourseLoadingUtils.publishSearching();
		// get subjects
		CourseService.initSchoolSubjects();
		// load courses
		dojo.xhrGet({ url:"schoolCourses.json", content:{ year:Constants.getSelectedYear(), term:Constants.getSelectedTerm(), school:Constants.selectedSchool }, handleAs:"json", error:Constants.ERROR,
			load:function(json) {
				Constants.courses = json;
				SchoolCourseUtils.refreshCourses(json);
			}
		});
	};
	// loadSubjects
	var loadSubjects = function(fragment) {
		console.log("loading subjects: " + fragment);
		// parse semester, campus, level
		if (1) {
			// semester
			var semester = UrlParserUtils.parse(fragment, "semester");
			Constants.selectedSemester = semester;
			// campus
			var campus = UrlParserUtils.parse(fragment, "campus");
			Constants.selectedCampus = campus;
			// level
			var level = UrlParserUtils.parse(fragment, "level");
			Constants.selectedLevel = level;
			// searchType
			Constants.searchType = Constants.LOCATION_SEARCH;
		}
		FormService.reset();
		// update house icon based on semester
		FormService.updateHouseIcon();
		// get subjects
		CourseService.initSubjects();
		// hide form, disclaimers, old version of soc link
		dojo.addClass("termlocationlevelform", "hidden");
		dojo.addClass("disclaimers", "hidden");
		MessageUtils.hideMessages();
		// unhide search/filters content
		dojo.removeClass("searchfilterscontent", "hidden");
		// init search tabs
		SearchTabUtils.initLocationSearchTab();
		// update semester title
		SemesterUtils.updateSemesterTitle();
		// add tooltips for campus list (search title)
		FormService.createCampusTooltipsInTitle();
		// unhide level of study filters based on user selection
		FilterService.initLevelOfStudyFilters();
		// unhide new brunswick sub-campus filters based on user selection
		FilterService.initNewBrunswickSubCampusFilters();
		// hide course types filters if online
		FilterService.initCourseTypesFilters();
		// hide day & time filters if online
		FilterService.initDayAndTimeFilters();
		// disable all filters initially
		FilterUtils.disableFilters();
		// unhide instructions
		InstructionUtils.showInstructions();
	};
	// loadHome
	var loadHome = function() {
		FormService.reset();
	};
	return {
		// backToHomePage
		backToHomePage: function() {
			if (Constants.isIframe) {
				window.location = Constants.iframeUrl;
			}
			else {
				window.location = "/soc";
			}
		},
		// loadContent
		loadContent: function(fragment) {
			fragment = unescape(fragment);
			console.log("loading content: " + fragment);
			if (fragment.indexOf("home") != -1) {
				loadHome();
				// show system message
				MessageUtils.showSystemMessage();
			}
			else if (fragment.indexOf("subjects?") != -1) loadSubjects(fragment);
			else if (fragment.indexOf("courses?") != -1) loadCourses(fragment);
			else if (fragment.indexOf("multi?") != -1) loadMulti(fragment);
			else if (fragment.indexOf("keyword?") != -1) loadKeyword(fragment);
			else if (fragment.indexOf("schools?") != -1) loadSchools(fragment);
			else if (fragment.indexOf("course?") != -1) loadUnitCourse(fragment);
			else if (fragment.indexOf("coreCode?") != -1) loadCoreCode(fragment);
		}
	};
})();

// CoreCodeCourseUtils
var CoreCodeCourseUtils = (function() {
	return {
		getCoreCodeCourses: function(coreCode) {
			if (!coreCode) return;
			var fragment = "coreCode?code=" + coreCode + "&semester=" + Constants.selectedSemester + "&campus=" + Constants.selectedCampus + "&level=" + Constants.selectedLevel;
			HistoryUtils.addHistory(fragment);
			ContentUtils.loadContent(fragment);
		},
		loadCourses: function(json) {
			// hide instructions
			InstructionUtils.hideInstructions();
			// check for null json object
			if (!json || json.length == 0) {
				CourseLoadingUtils.publishCoursesFound(0);
				return;
			}
			// empty target html
			CourseDisplayUtils.clearAllCourses();
			// publish courses found
			CourseLoadingUtils.publishCoursesFound(json);
			// refresh subject title
			CourseDisplayUtils.refreshSubjectTitle(CoreCodeUtils.getCoreCodeLabel(Constants.currentCoreCode));
			// refresh show/hide all button
			CourseDisplayUtils.refreshExpandAllButton();
			// refresh course list
			CourseDisplayUtils.refreshCoursesDisplay(json);
			// create prereq tooltips
			PrereqUtils.createPrereqTooltips();
			// append school filters
			var schools = SchoolUtils.getSchoolsFromCourses(json);
			FilterService.appendSchoolFilters(schools);
			dojo.byId("school_filters_div").style.display = "";
			// append subject filters
			var subjects = SubjectUtils.getSubjectsFromCourses(json);
			FilterService.appendKeywordSubjectFilters(subjects);
			dojo.byId("subject_filters_div").style.display = "";
			// append core curriculum filters
			var cores = CoreCodeUtils.getCoreCodesFromCourses(json);
			FilterService.appendCoreCurriculumFilters(cores);
			// append course credit filters
			var credits = CourseCreditUtils.getCredits(json);
			FilterService.appendCourseCreditFilters(credits);
			// reset all filters
			FilterService.resetAllFilters();
			// enable filters
			FilterUtils.enableFilters();
		}
	};
})();

// CoreCodeSearchUtils
var CoreCodeSearchUtils = (function() {
	var _filteringselect = null;
	var _initFilteringSelect = function() {
		// create new div object to hold widget
		var div = dojo.create("div", null, "coreCodeSearchFilteringSelectDiv");
		// create filteringselect
		_filteringselect = new dijit.form.FilteringSelect({
			store:new dojo.data.ItemFileReadStore({ data:CoreCodeSearchUtils.buildItemFileReadStore() }),
			displayedValue:CoreCodeUtils.getCoreCodeLabel(Constants.currentCoreCode),
			searchAttr:"label",
			labelAttr:"label",
			style:"border:2px solid #d21033;font-size:10px;width:295px;padding:7px;margin:0 auto;display:block;"
		}, div);
		// connect widget to javascript event: onChange
		dojo.connect(dijit.byId(_filteringselect), "onChange", function(coreCode) { CoreCodeCourseUtils.getCoreCodeCourses(coreCode); });
	};
	return {
		initCoreCodeFilteringSelectWidget: function() {
			if (!_filteringselect) _initFilteringSelect();
		},
		buildItemFileReadStore: function() {
			var data = new Object();
			data.identifier = "code";
			data.label = "label";
			data.items = Constants.CORE_CODES;
			return data;
		},
		clearFilteringSelectLabel: function() {
			if (_filteringselect) dijit.byId(_filteringselect).attr("displayedValue", "");
		}
	};
})();

// KeywordCourseUtils
var KeywordCourseUtils = (function() {
	return {
		// loadCourses
		loadCourses: function(json) {
			// hide instructions
			InstructionUtils.hideInstructions();
			// check for null json object
			if (!json || json.length == 0) {
				CourseLoadingUtils.publishCoursesFound(0);
				return;
			}
			// empty target html
			CourseDisplayUtils.clearAllCourses();
			// publish courses found
			CourseLoadingUtils.publishCoursesFound(json);
			// refresh subject title
			CourseDisplayUtils.refreshSubjectTitle(Constants.currentKeyword);
			// refresh show/hide all button
			CourseDisplayUtils.refreshExpandAllButton();
			// refresh course list
			CourseDisplayUtils.refreshCoursesDisplay(json, Constants.currentKeyword);
			// create prereq tooltips
			PrereqUtils.createPrereqTooltips();
			// append school filters
			var schools = SchoolUtils.getSchoolsFromCourses(json);
			FilterService.appendSchoolFilters(schools);
			dojo.byId("school_filters_div").style.display = "";
			// append subject filters
			var subjects = SubjectUtils.getSubjectsFromCourses(json);
			FilterService.appendKeywordSubjectFilters(subjects);
			dojo.byId("subject_filters_div").style.display = "";
			// append core curriculum filters
			var cores = CoreCodeUtils.getCoreCodesFromCourses(json);
			FilterService.appendCoreCurriculumFilters(cores);
			// append course credit filters
			var credits = CourseCreditUtils.getCredits(json);
			FilterService.appendCourseCreditFilters(credits);
			// reset all filters
			FilterService.resetAllFilters();
			// enable filters
			FilterUtils.enableFilters();
			// reset keyword search filters
			KeywordFilterUtils.resetKeywordSearchFilters();
		},
		// submitKeyword
		submitKeyword: function(form) {
			// validate keyword length
			var keyword = dojo.trim(dojo.byId("keyword_textbox_id").value);
			if (keyword.length < 3) {
				alert("Please enter at least 3 characters for the keyword search.");
				return;
			}
			// update Constants.currentKeyword
			Constants.currentKeyword = keyword.toUpperCase();
			var fragment = "keyword?keyword=" + Constants.currentKeyword + "&semester=" + Constants.selectedSemester + "&campus=" + Constants.selectedCampus + "&level=" + Constants.selectedLevel;
			HistoryUtils.addHistory(fragment);
			ContentUtils.loadContent(fragment);
		}
	};
})();

// MultiSubjectCourseUtils
var MultiSubjectCourseUtils = (function() {
	var multiDialog;
	return {
		// loadCourses
		loadCourses: function(json) {	
			// hide instructions
			InstructionUtils.hideInstructions();
			// validate result count
			if (json == null || json.length == 0) {
				CourseLoadingUtils.publishCoursesFound(0);
				return;
			}
			// empty target html
			CourseDisplayUtils.clearAllCourses();
			// publish courses found
			CourseLoadingUtils.publishCoursesFound(json);
			// refresh subject title
			var title = SubjectUtils.getSubjectTitles(Constants.selectedSubject);
			CourseDisplayUtils.refreshSubjectTitle(title);
			// refresh show/hide all button
			CourseDisplayUtils.refreshExpandAllButton();
			// refresh course list
			CourseDisplayUtils.refreshCoursesDisplay(json);
			// create prereq tooltips
			PrereqUtils.createPrereqTooltips();
			// append school filters
			var schools = SchoolUtils.getSchoolsFromCourses(json);
			FilterService.appendSchoolFilters(schools);
			dojo.byId("school_filters_div").style.display = "";
			// append subject filters
			FilterService.appendSubjectFilters(Constants.selectedSubject);
			dojo.byId("subject_filters_div").style.display = "";
			// append core curriculum filters
			var cores = CoreCodeUtils.getCoreCodesFromCourses(json);
			FilterService.appendCoreCurriculumFilters(cores);
			// append course credit filters
			var credits = CourseCreditUtils.getCredits(json);
			FilterService.appendCourseCreditFilters(credits);
			// reset all filters
			FilterService.checkAllFilters();
			// enable filters
			FilterUtils.enableFilters();
		},
		// updateSubjectCount
		updateSubjectCount: function() {
			var count = 0;
			dojo.query("input[name=subject_multiple]").forEach(function(item) {
				if (item.checked) count++;
			});
			var html = (count > Constants.MULTIPLE_SUBJECTS_MAX_COUNT) ? "<span style='color:#d21033;'>" + count + "</span>" : count;
			dojo.byId("multiSubjectCurrentCount").innerHTML = html;
		},
		// reset
		reset: function() {
			if (multiDialog) { 
				multiDialog.destroyRecursive(); 
				multiDialog = null; 
			}
		},
		// loadMultipleSubjectDialog
		loadMultipleSubjectDialog: function() {
			// check if dialog already exists
			if (multiDialog) {
				multiDialog.show();
				return;
			}
			var content = "";
			dojo.forEach(Constants.currentSubjects, function(subject) { 
				content += "<div class='subject_multiple_div' onclick='FormService.clickChildNode(this)'><input type='checkbox' name='subject_multiple' value='" + subject.code + "' class='subject_multiple' onclick='FormService.clickChildNode(this.parentNode);MultiSubjectCourseUtils.updateSubjectCount();'> <span class='multiSubjectListingText'>" + subject.label + "<span></div>";
			});
			// create dialog
			multiDialog = new dijit.Dialog({
				title:"<span id='multiSubjectDialogHeader'>SUBJECTS: <span id='multiSubjectCurrentCount'>0</span> / <span id='multiSubjectMaxCount'>" + Constants.MULTIPLE_SUBJECTS_MAX_COUNT + "</span></span>" +
					"<button id='multiSubjectSubmit' class='multiSubjectButton' onclick='MultiSubjectCourseUtils.submitMultipleSubjectDialog()'>Submit</button>" + 
					"<span id='multiSubjectSubmitResetSpacer'></span>" +
					"<button id='multiSubjectReset' class='multiSubjectButton' onclick='MultiSubjectUtils.clearAllSelections()'>Reset</button>",
				content:content
			});
			multiDialog.show();
		},
		// submitMultipleSubjectDialog
		submitMultipleSubjectDialog: function() {
			// get subjects, subject count
			var subjects = "";
			var count = 0;
			dojo.query("input.subject_multiple").forEach(function(item) { 
				if (item.checked) {
					subjects += item.value + ",";
					count++;
				}
			});
			// validate subject count
			if (count > Constants.MULTIPLE_SUBJECTS_MAX_COUNT) {
				alert("Please limit the number of your selections to " + Constants.MULTIPLE_SUBJECTS_MAX_COUNT);
				return;
			}
			else if (count == 0) {
				alert("Please select at least one subject");
				return;
			}
			subjects = subjects.substring(0, subjects.length - 1);
			Constants.selectedSubject = subjects;

			// hide dialog
			if (multiDialog) multiDialog.hide();
			
			var fragment = "multi?subject=" + Constants.selectedSubject + "&semester=" + Constants.selectedSemester + "&campus=" + Constants.selectedCampus + "&level=" + Constants.selectedLevel;
			HistoryUtils.addHistory(fragment);
			ContentUtils.loadContent(fragment);
		}
	};
})();

// SchoolCourseUtils
var SchoolCourseUtils = (function() {
	return {
		getCourses: function(schoolCode) {
			if (!schoolCode) return;
			var fragment = "schools?semester=" + Constants.selectedSemester + "&school=" + schoolCode;
			HistoryUtils.addHistory(fragment);
			ContentUtils.loadContent(fragment);
		},
		refreshCourses: function(json) {
			// validate result count
			if (json == null || json.length == 0) {
				CourseLoadingUtils.publishCoursesFound(0);
				return;
			}			
			// empty target html
			CourseDisplayUtils.clearAllCourses();			
			// publish courses found
			CourseLoadingUtils.publishCoursesFound(json);			
			// refresh subject title
			var title = SchoolUtils.getSchoolLabel(Constants.selectedSchool);
			SchoolCourseDisplayUtils.refreshSubjectTitle(title);
			// refresh show/hide all button
			CourseDisplayUtils.refreshExpandAllButton();
			// delay course display
			Constants.refreshCoursesInterval = setInterval("SchoolCourseDisplayUtils.displayCourses()", 400);
		}
	};
})();

// SchoolSearchUtils
var SchoolSearchUtils = (function() {
	var _filteringselect = null;
	var _initFilteringSelect = function() {
		// create new div object to hold widget
		var div = dojo.create("div", null, "filteringSelectDiv");
		// create filteringselect
		_filteringselect = new dijit.form.FilteringSelect({
			store:new dojo.data.ItemFileReadStore({ data:SchoolSearchUtils.buildSchoolItemFileReadStore() }),
			displayedValue:SchoolUtils.getSchoolLabel(Constants.selectedSchool),
			searchAttr:"label",
			labelAttr:"label",
			style:"border:2px solid #d21033;font-size:10px;width:295px;padding:7px;margin:0 auto;display:block;"
		}, div);
		// connect widget to javascript event: onChange
		dojo.connect(dijit.byId(_filteringselect), "onChange", function(schoolCode) { SchoolCourseUtils.getCourses(schoolCode); });
	};
	return {
		initSchoolFilteringSelectWidget: function() {
			if (!_filteringselect) _initFilteringSelect();
		},
		clearSchoolFilteringSelectLabel: function() {
			if (_filteringselect) dijit.byId(_filteringselect).attr("displayedValue", "");
		},
		buildSchoolItemFileReadStore: function() {
			var schools = [];
			dojo.forEach(Constants.SCHOOLS, function(item) {
				item.label = SchoolUtils.getSchoolLabel(item.code);
				schools.push(item);
			});
			schools.sort(function(a, b) {
				var result = -1;
				if (a.description > b.description) result = 1;
				return result;
			});
			var store = new Object();
			store.identifier = "code";
			store.label = "label";
			store.items = schools;
			return store;
		}
	};
})();

// SingleSubjectCourseUtils
var SingleSubjectCourseUtils = (function() {
	return {
		getCourses: function(subjectCode) {
			if (!subjectCode) return;
			var fragment = "courses?subject=" + subjectCode + "&semester=" + Constants.selectedSemester + "&campus=" + Constants.selectedCampus + "&level=" + Constants.selectedLevel;
			HistoryUtils.addHistory(fragment);
			ContentUtils.loadContent(fragment);
		},
		refreshCourses: function(subjectCode, json) {
			// hide instructions
			InstructionUtils.hideInstructions();
			// validate result count
			if (json == null || json.length == 0) {
				CourseLoadingUtils.publishCoursesFound(0);
				return;
			}
			// empty target html
			CourseDisplayUtils.clearAllCourses();
			// publish courses found
			CourseLoadingUtils.publishCoursesFound(json);
			// refresh subject title
			CourseDisplayUtils.refreshSubjectTitle(SubjectUtils.getSubjectLabel(subjectCode));
			// refresh show/hide all button
			CourseDisplayUtils.refreshExpandAllButton();
			// refresh course list
			CourseDisplayUtils.refreshCoursesDisplay(json);
			// create prereq tooltips
			PrereqUtils.createPrereqTooltips();
			// append school filters
			var schools = SchoolUtils.getSchoolsFromCourses(json);
			FilterService.appendSchoolFilters(schools);
			dojo.byId("school_filters_div").style.display = "";
			// hide subject filters
			dojo.byId("subject_filters_div").style.display = "none";
			// append core curriculum filters
			var cores = CoreCodeUtils.getCoreCodesFromCourses(json);
			FilterService.appendCoreCurriculumFilters(cores);
			// append course credit filters
			var credits = CourseCreditUtils.getCredits(json);
			FilterService.appendCourseCreditFilters(credits);
			// reset all filters
			FilterService.checkAllFilters();
			// enable filters
			FilterUtils.enableFilters();
		}
	};
})();

// SingleSubjectSearchUtils
var SingleSubjectSearchUtils = (function() {
	var _filteringselect = null;
	var _initFilteringSelect = function() {
		// create new div object to hold widget
		var div = dojo.create("div", null, "filteringSelectDiv");
		// create filteringselect
		_filteringselect = new dijit.form.FilteringSelect({
			store:new dojo.data.ItemFileReadStore({ data:SingleSubjectSearchUtils.buildSubjectItemFileReadStore() }),
			displayedValue:SubjectUtils.getSubjectLabel(Constants.selectedSubject),
			searchAttr:"label",
			labelAttr:"label",
			style:"border:2px solid #d21033;font-size:10px;width:295px;padding:7px;margin:0 auto;display:block;"
		}, div);
		// connect widget to javascript event: onChange
		dojo.connect(dijit.byId(_filteringselect), "onChange", function(subjectCode) { SingleSubjectCourseUtils.getCourses(subjectCode); });
	};
	return {
		initSubjectFilteringSelectWidget: function() {
			if (!_filteringselect) _initFilteringSelect();
			else {
				dijit.byId(_filteringselect).attr("store", new dojo.data.ItemFileReadStore({ data:SingleSubjectSearchUtils.buildSubjectItemFileReadStore() }));
			}
		},
		clearSubjectFilteringSelectLabel: function() {
			if (_filteringselect) dijit.byId(_filteringselect).attr("displayedValue", "");
		},
		buildSubjectItemFileReadStore: function() {
			var data = new Object();
			data.identifier = "code";
			data.label = "label";
			data.items = Constants.currentSubjects;
			return data;
		}
	};
})();

// BookUtils
var BookUtils = (function() {
	var _buildLink = function(course, section, campus, storeId) {
		// build link based on course/section info
		var link = [];
		link.push("<a target='_blank' href=\"https://securex.bncollege.com/webapp/wcs/stores/servlet/TBListView?catalogId=10001&storeId=");
		link.push(storeId);
		link.push("&courseXml=<textbookorder>");
		if (campus == "CM") link.push("<campus name='RUTGERS'>");
		link.push("<courses><course num='");
		link.push(course.courseNumber);
		link.push("' dept='");
		link.push(course.subject);
		link.push("' sect='");
		link.push(section.number);
		link.push("' term='");
		link.push(FormService.getSemesterTermValueTwoDigitYear());
		link.push("'></course></courses>");
		if (campus == "CM") link.push("</campus>");
		link.push("</textbookorder>");
		link.push("\">Books</a>");
		return link.join("");
	};
	var _getCampus = function(course, section) {
		var campus = course.campusCode;
		if (campus == "OB") campus = "NB";
		else if (campus == "ON") campus = "NK";
		else if (campus == "OC") campus = "CM";
		if (campus != "NB" && campus != "NK" && campus != "CM") {
			// set default campus to NB
			campus = "NB";
//			var unit = course.offeringUnitCode;
//			campus = SchoolUtils.getCampusCode(unit);			
		}
		return campus;
	};
	return {
		// getBooksLink
		getBooksLink: function(course, section) {
			var campus = _getCampus(course, section);
			var storeId = Constants.BOOKSTORES[campus];
			var link = _buildLink(course, section, campus, storeId);
			return link;
		}
	};
})();

// BrowserUtils
var BrowserUtils = (function() {
	return {
		detectVersion: function() {
			// ie
			if (dojo.isIE) {
				dojo.removeClass("ie", "browser");
				if (dojo.isIE >= 7) dojo.addClass("ie", "browserVersionMatch");
				else dojo.addClass("ie", "browserVersionFail");
			}
			// firefox
			else if (dojo.isFF) {
				dojo.removeClass("firefox", "browser");
				dojo.addClass("firefox", dojo.isFF >= 3 ? "browserVersionMatch" : "browserVersionFail");
			}
			// chrome
			else if (dojo.isChrome) {
				dojo.removeClass("chrome", "browser");
				dojo.addClass("chrome", dojo.isChrome >= 13 ? "browserVersionMatch" : "browserVersionFail");
			}
			// safari
			else if (dojo.isSafari) {
				dojo.removeClass("safari", "browser");
				dojo.addClass("safari", dojo.isSafari >= 5 ? "browserVersionMatch" : "browserVersionFail");
			}
		}
	};
})();

// BuildingUtils
var BuildingUtils = (function() {
	return {
		// getBuildingAndRoom
		getBuildingAndRoom: function(time) {
			if (time.buildingCode == null || time.roomNumber == null) return "";
			return time.buildingCode + "-" + time.roomNumber;
		},
		// getBuildingDescription
		getBuildingDescription: function(buildingAndRoom) {
			if (buildingAndRoom == null || buildingAndRoom == "") return null;
			var buildingCode = buildingAndRoom.substring(0, buildingAndRoom.indexOf("-"));
			buildingCode = dojo.trim(buildingCode.toUpperCase());
			return BuildingUtils.getBuildingNameUsingCode(buildingCode);
		},
		// getBuildingNameUsingCode
		getBuildingNameUsingCode: function(code) {
			var name;
			dojo.forEach(Constants.BUILDINGS, function(building) {
				if (building.code == code) name = building.name;
			});
			return name ? name : "N/A";
		},
		// getBuildingObject
		getBuildingObject: function(code) {
			var tmp;
			dojo.forEach(Constants.BUILDINGS, function(item) {
				if (item.code == code) tmp = item;
			});
			return tmp;
		}
	};
})();

// CampusSelectionUtils
var CampusSelectionUtils = (function() {
	var toggleCounter = 0;
	// clearCampusLocations
	var clearCampusLocations = function() {
		dojo.query(".onCampusLocation").forEach(function(item) { item.checked = false; });
		dojo.query(".offCampusLocation").forEach(function(item) { item.checked = false; });
	};
	// selectOnCampusLocations
	var selectOnCampusLocations = function() {
		dojo.query(".onCampusLocation").forEach(function(item) { dojo.byId(item).click(); });
	};
	// selectOffCampusLocations
	var selectOffCampusLocations = function() {
		dojo.query(".offCampusLocation").forEach(function(item) { dojo.byId(item).click(); });
	};
	return {
		// toggleCampusSelections
		toggleCampusSelections: function() {
			clearCampusLocations();
			var modulo = toggleCounter++ % 4;
			if (modulo == 0) {
				selectOnCampusLocations();
			}
			else if (modulo == 1) {
				selectOnCampusLocations();
				selectOffCampusLocations();
			}
			else if (modulo == 2) {
				selectOffCampusLocations();
			}
			else {
				// leave selections cleared
			}
		},
		// selectOnline
		selectOnline: function(isOnline) {
			if (isOnline) {
				if (dojo.byId("campusOnlineOption").checked) {
					dojo.query("input[name=campus]").forEach(function(item) {
						if (item.value != "ONLINE") item.checked = false;
					});
				}
			}
			else {
				dojo.query("input[name=campus]").forEach(function(item) {
					if (item.value == "ONLINE") item.checked = false;
				});
			}
		}
	};
})();

// CampusUtils
var CampusUtils = (function() {
	return {
		// isCampusSelected
		isCampusSelected: function(campusCode) {
			return dojo.some(Constants.selectedCampus.split(","), function(campus) { return campus == campusCode; });
		},
		// isOffCampus
		isOffCampus: function(code) {
			return !(code == "NB" || code == "NK" || code == "CM");
		},
		// getSubcampusCode
		getSubcampusCode: function(time) {
			return time.campusAbbrev != null ? time.campusAbbrev : "";
		},
		// getSubcampusFilters
		getSubcampusFilters: function(section) {
			var filters = "";
			dojo.forEach(section.meetingTimes, function(time) {
				var campusCode = CampusUtils.getSubcampusCode(time);
				filters += " " + campusCode;
			});
			return filters;
		},
		// getSubcampusDescription
		getSubcampusDescription: function(campusCode) {
			if (Constants.SUBCAMPUSES[campusCode]) return Constants.SUBCAMPUSES[campusCode];
			return campusCode;
		},
		// getCampusName
		getCampusName: function(code) {
			var tmp;
			dojo.forEach(Constants.CAMPUSES, function(item) {
				if (code == item.code) tmp = item.name;
			});
			return tmp;
		}
	};
})();

// CoreCodeUtils
var CoreCodeUtils = (function() {
	return {
		getCoreCodeLabel: function(code) {
			var label = "";
			dojo.forEach(Constants.CORE_CODES, function(item) {
				if (item.code == code) {
					label = item.label;
					return;
				}
			});
			return label;
		},
		getCoreCodesFromCourses: function(courses) {
			var items = [];
			dojo.forEach(courses, function(course) {
				dojo.forEach(course.coreCodes, function(coreCode) {
					var item = { code:coreCode.code, name:coreCode.description };
					items.push(item);
				});
			});
			// convert list to map to remove duplicates
			var map = {};
			dojo.forEach(items, function(item) {
				map[item.code] = item.name;
			});
			// convert map back to list
			items = [];
			for (var key in map) {
				var item = { code:key, name:map[key] };
				items.push(item);
			}
			// sort list
			return items.sort(function(a, b) { return a.code > b.code; });
		}
	};
})();

// CourseCreditUtils
var CourseCreditUtils = (function() {
	return {
		getCourseCreditsFilterClassName: function(course) {
			var name = (course.credits == null ? "BA" : course.credits) + "";
			name = name.replace(/\./g, "_");
			return "courseCredit_" + name;
		},
		getCredits: function(courses) {
			var items = [];
			dojo.forEach(courses, function(course) {
				// make sure at least one section of a course is enabled
				if (SectionUtils.isAllSectionsPrintFlagDisabled(course.sections)) return;
				var item = {};
				// if null credits, set to by arrangement
				if (course.credits == null) {
					item.code = "BA";
					item.name = "Credits by arrangement";
				}
				else {
					item.code = course.credits;
					item.name = course.credits + " credit(s)";
				}
				items.push(item);
			});
			// convert list to map to remove duplicates
			var map = {};
			dojo.forEach(items, function(item) {
				map[item.code] = item.name;
			});
			// convert map back to list
			items = [];
			for (var key in map) {
				var item = { code:key, name:map[key] };
				items.push(item);
			}
			// sort list
			items = items.sort(function(a, b) { 
				// sort by arrangement credits last
				if (a.code == "BA") return 1;
				if (b.code == "BA") return -1;
				// convert credits to floats
				var a1 = parseFloat(a.code);
				var b1 = parseFloat(b.code);
				// return IE friendly values
				if (a1 > b1) return 1;
				if (a1 < b1) return -1;
				return 0;
			});
			return items;
		}
	};
})();

// CourseDisplayUtils
var CourseDisplayUtils = (function() {
	return {
		// clearAllCourses
		clearAllCourses: function() {
			dojo.empty(Constants.COURSES_OUTPUT_ID);
		},
		// refreshSubjectTitle
		refreshSubjectTitle: function(title) {
			var id = "subjectTitle";
			dojo.destroy(id);
			dojo.create("h4", { id:id }, Constants.COURSES_OUTPUT_ID);
			// add subject title, plus minus to expand/collapse all
			var span = dojo.create("span", { innerHTML:"<u>+</u>&nbsp;&nbsp;" + title }, id);
			dojo.addClass(dojo.byId(span), "cursortext");
			// add click to expand/collapse all function
			dojo.connect(dojo.byId(id), "onclick", null, CourseService.toggleExpandAllCourses);
			// add subject title tooltip > click to expand/collapse
			new dijit.Tooltip({ connectId:id, label:"<span id='subjectTitleTooltip'>Expand/Collapse All " + Constants.coursesFound + " Courses</span>", showDelay:0, position:["above","below","after"] });
		},			
		// refreshExpandAllButton
		refreshExpandAllButton: function() {
			var id = "expandAllSpanId";
			dojo.destroy(id);
			//dojo.create("span", { id:id, innerHTML:"expand/collapse all", onclick:CourseService.toggleExpandAllCourses }, "showHideAllDiv");
			dojo.create("span", { id:id, innerHTML:"&nbsp;" }, "showHideAllDiv");
		},
		// refreshCoursesDisplay
		refreshCoursesDisplay: function(json, keyword) {
			// check if restricted sections exist > show section filter
			if (SectionUtils.containsRestrictedSection(json)) {
				dojo.byId("restrictedStatusFilter").style["display"] = "";
			}
			else {
				dojo.byId("restrictedStatusFilter").style["display"] = "none";
			}
			// create parent div
			var div = dojo.create("div", { id:"courseDataParent" });
			// append course display
			dojo.forEach(json, function(item, i) { CourseDisplayUtils.refreshCourseDisplay(item, i, div, keyword); });
			// append parent div to output
			dojo.byId(Constants.COURSES_OUTPUT_ID).appendChild(div);
			// unhide back to top link
			dojo.removeClass("backToTop", "hidden");
		},
		// refreshCourseDisplay
		refreshCourseDisplay: function(course, i, div, keyword) {
			
			// course id
			var courseId = course.offeringUnitCode + ":" + course.subject + ":" + course.courseNumber + "." + i;

			// subject id
			var subjectId = "subject." + course.subject + "." + courseId;
			var subjectIdDiv = dojo.create("div", { id:subjectId }, div);
			dojo.addClass(subjectIdDiv, "subject");
			
			// course list
			var courseListId = subjectId + ".courseList";
			var courseListIdDiv = dojo.create("div", { id:courseListId }, subjectIdDiv);
			
			// check if all sections have the printed flag disabled
			if (SectionUtils.isAllSectionsPrintFlagDisabled(course.sections)) return;
			
			// course
			var courseIdDiv = dojo.create("div", { id:courseId }, courseListIdDiv);
			
			// course filters
			dojo.addClass(courseIdDiv, "courseItem");
			dojo.addClass(courseIdDiv, "courseLevel_" + course.courseNumber.substr(0, 1) + "00");
			dojo.addClass(courseIdDiv, "school_" + course.offeringUnitCode);
			dojo.addClass(courseIdDiv, "subject_" + course.subject);

			// course filters - core codes
			dojo.forEach(course.coreCodes, function(coreCode) {
				dojo.addClass(courseIdDiv, "coreCode_" + coreCode.code);
			});
			if (course.coreCodes.length == 0) dojo.addClass(courseIdDiv, "coreCode_NA");

			// course filters - course credits
			var courseCreditsFilterClassName = CourseCreditUtils.getCourseCreditsFilterClassName(course);
			dojo.addClass(courseIdDiv, courseCreditsFilterClassName);

			// course metadata (used for easier keyword filtering)
			var courseMetadataId = courseId + ".courseMetadata";
			var courseMetadataIdDiv = dojo.create("div", { id:courseMetadataId }, courseIdDiv);
			dojo.addClass(courseMetadataIdDiv, "metadata");
			dojo.addClass(courseMetadataIdDiv, "hidden");

			// course metadata - add title, extended title
			dojo.create("span", { id:courseMetadataId + ".title", innerHTML:course.title }, courseMetadataIdDiv);
			dojo.create("span", { id:courseMetadataId + ".extendedTitle", innerHTML:course.expandedTitle }, courseMetadataIdDiv);

			// course info
			var courseInfoId = courseId + ".courseInfo";
			var courseInfoIdDiv = dojo.create("div", { id:courseInfoId }, courseIdDiv);
			dojo.addClass(courseInfoIdDiv, "courseInfo");

			// spacer
			dojo.create("span", { id:"courseSpacer", innerHTML:"&nbsp;" }, courseInfoIdDiv);

			// arrow icon
			var courseExpandIconDiv = dojo.create("span", { id:courseId + ".courseExpandIcon", innerHTML:"<img src='images/right_arrow.png' onclick='CourseService.expandOrCollapseCourse(\"" + courseId + "\");' />" }, courseInfoIdDiv);
			dojo.addClass(courseExpandIconDiv, "courseExpandIcon");

			// course numbers
			var courseNum = courseId.substr(0, courseId.lastIndexOf("."));
			var courseIdSpan = dojo.create("span", { id:"courseId", innerHTML:"<span class='highlighttext' onclick='CourseService.expandOrCollapseCourse(\"" + courseId + "\");'><span id='courseId." + courseNum + "'>" + courseNum + "</span></span>" }, courseInfoIdDiv);
			var courseNumTooltip = "<b>" + course.offeringUnitCode + "</b> [" + SchoolUtils.getSchoolName(course.offeringUnitCode) + "] : <b>" + course.subject + "</b> [" + SubjectUtils.getSubjectDescription(course.subject) + "] : <b>" + course.courseNumber + "</b> [" + course.title + "]";
			new dijit.Tooltip({ connectId:courseIdSpan, label:courseNumTooltip, showDelay:0, position:["above", "below", "after", "before"] });

			// course title, credits
			var courseTitleAndCreditsId = courseId + ".courseTitleAndCredits";
			var courseTitleAndCreditsIdDiv = dojo.create("span", { id:courseTitleAndCreditsId }, courseInfoIdDiv);
			dojo.addClass(courseTitleAndCreditsIdDiv, "courseTitleAndCredits");

			// course title
			var courseTitleId = courseTitleAndCreditsId + ".courseTitle";
			var isExpandedTitle = course.expandedTitle && dojo.trim(course.expandedTitle).length > 0;
			var title = isExpandedTitle ? dojo.trim(course.expandedTitle) : dojo.trim(course.title);
			var titleMaxLength = 55;
			var titleDisplay = (title.length > titleMaxLength ? title.substring(0, titleMaxLength) + ".." : title);
			titleDisplay = "<span class='highlighttext' onclick='CourseService.expandOrCollapseCourse(\"" + courseId + "\");'>" + titleDisplay + "</span>";
			// title highlighting when keyword searched
			if (keyword) {
				// trim, uppercase
				keyword = dojo.trim(keyword.toUpperCase());
				// splice keyword out of string
				var index = titleDisplay.toUpperCase().indexOf(keyword);
				if (index >= 0) {
					var s1 = titleDisplay.substring(0, index);
					var s2 = titleDisplay.substring(index + keyword.length);
					// wrap span tags around keyword text
					var tmp = "<span class='keywordTitle'>" + keyword + "</span>";
					// replace titleDisplay with modified keyword text
					titleDisplay = s1 + tmp + s2;
				}
			}
			var titleTooltipDisplay = (title.length > titleMaxLength ? title + " / " + course.title : course.title);
			var courseTitleIdDiv = dojo.create("span", { id:courseTitleId, innerHTML:titleDisplay }, courseTitleAndCreditsIdDiv);
			dojo.addClass(courseTitleIdDiv, "courseTitle");
			// title: tooltip
			if (isExpandedTitle) new dijit.Tooltip({ connectId:courseTitleIdDiv, label:titleTooltipDisplay, showDelay:0, position:["above", "below", "after", "before"] });

			// course credits
			var courseCreditsId = courseTitleAndCreditsId + ".courseCredits";
			var courseCreditsIdDiv = dojo.create("span", { id:courseCreditsId, innerHTML:(course.credits == null ? "Credits by arrangement" : course.credits + " credit(s)") }, courseTitleAndCreditsIdDiv);
			dojo.addClass(courseCreditsIdDiv, "courseCredits");

			// open sections
			var courseOpenSectionsId = courseId + ".courseOpenSections";
			var courseOpenSectionsIdDiv = dojo.create("span", { id:courseOpenSectionsId }, courseInfoIdDiv);
			dojo.addClass(courseOpenSectionsIdDiv, "courseOpenSections");

			var courseOpenSectionsDataId = courseId + ".courseOpenSectionsData";
			var courseOpenSectionsDataIdDiv = dojo.create("span", { id:courseOpenSectionsDataId, innerHTML:"Sections: " }, courseOpenSectionsIdDiv);

			var courseOpenSectionsNumeratorId = courseOpenSectionsId + ".courseOpenSectionsNumerator";
			var courseOpenSectionsNumeratorIdDiv = dojo.create("span", { id:courseOpenSectionsNumeratorId, innerHTML:SectionUtils.getOpenSections(course) }, courseOpenSectionsDataIdDiv);
			dojo.addClass(courseOpenSectionsNumeratorIdDiv, "courseOpenSectionsNumerator");

			var courseOpenSectionsDenominatorId = courseOpenSectionsId + ".courseOpenSectionsDenominator";
			var courseOpenSectionsDenominatorIdDiv = dojo.create("span", { id:courseOpenSectionsDenominatorId, innerHTML:"&nbsp;/&nbsp;" + SectionUtils.getTotalSections(course) }, courseOpenSectionsDataIdDiv);
			dojo.addClass(courseOpenSectionsDenominatorIdDiv, "courseOpenSectionsDenominator");
			// open sections: tooltip
			new dijit.Tooltip({ connectId:[courseOpenSectionsDataIdDiv], label:("Open Sections: <span style='color:#060;font-weight:bold;'>" + SectionUtils.getOpenSections(course) + "</span> / Total Sections: " + SectionUtils.getTotalSections(course)), showDelay:0, position:["above", "below", "before", "after"] });

			// course prereqs
			var coursePrereqId = courseId + ".prereq";
			var prereqNotes = course.preReqNotes ? dojo.trim(course.preReqNotes) : null;
			var coursePrereqIdDiv = dojo.create("span", { id:coursePrereqId, innerHTML:(prereqNotes ? "<span id='" + coursePrereqId + "' onclick='CourseService.loadPrereqDialog(this);'>Prereqs</span>" : "<span class='hiddenPrereqsLink'>Prereqs</span>") }, courseInfoIdDiv);
			dojo.addClass(coursePrereqIdDiv, "prereq");
			// prereq tooltips
			if (course.preReqNotes != null) PrereqUtils.addPrereqTooltip(coursePrereqId, course.preReqNotes);

			// course synopsis
			var courseSynopsisId = courseId + ".synopsis";
			var synopsisUrl = course.synopsisUrl ? dojo.trim(course.synopsisUrl) : null;
			var courseSynopsisIdDiv = dojo.create("span", { id:courseSynopsisId, innerHTML:(synopsisUrl ? "<span onclick='CourseService.openSynopsis(\"" + synopsisUrl + "\");' class='synopsis'>Synopsis</span>" : "") }, courseInfoIdDiv);
			dojo.addClass(courseSynopsisIdDiv, "synopsis");

			// course data
			var courseDataId = courseId + ".courseData";
			var courseDataIdDiv = dojo.create("div", { id:courseDataId }, courseIdDiv);
			dojo.addClass(courseDataIdDiv, "courseData");

			// course notes header: course description, course notes, school notes
			var courseNotesHeaderId = courseId + ".courseNotesHeader";
			var courseNotesHeaderIdDiv = dojo.create("div", { id:courseNotesHeaderId }, courseDataIdDiv);
			dojo.addClass(courseNotesHeaderIdDiv, "courseNotesHeader");
			dojo.addClass(courseNotesHeaderIdDiv, "hidden3");

			// course description
			if (course.courseDescription) {
				var courseDescriptionId = courseId + "courseDescription";
				var courseDescriptionIdDiv = dojo.create("div", { id:courseDescriptionId, innerHTML:"<span>Course Description:</span> " + (course.courseDescription) }, courseNotesHeaderIdDiv);
				dojo.addClass(courseDescriptionIdDiv, "courseDescription");
			}

			// course notes
			if (course.courseNotes) {
				var courseNotesId = courseId + "courseNotes";
				var courseNotesIdDiv = dojo.create("div", { id:courseNotesId, innerHTML:"<span>Course Notes:</span> " + (course.courseNotes) }, courseNotesHeaderIdDiv);
				dojo.addClass(courseNotesIdDiv, "courseNotes");
			}

			// course supplement code
			if (course.supplementCode && dojo.trim(course.supplementCode)) {
				var courseSupplementCodeId = courseId + ".supplementCode";
				var courseSupplementCodeIdDiv = dojo.create("div", { id:courseSupplementCodeId, innerHTML:"<span>Course Supplement Code:</span> " + (course.supplementCode) }, courseNotesHeaderIdDiv);
				dojo.addClass(courseSupplementCodeIdDiv, "supplementCode");
			}

			// course school/unit notes
			if (course.unitNotes) {
				var schoolNotesId = courseId + "schoolNotes";
				var schoolNotesIdDiv = dojo.create("div", { id:schoolNotesId, innerHTML:"<span>School Notes:</span> " + (course.unitNotes) }, courseNotesHeaderIdDiv);
				dojo.addClass(schoolNotesIdDiv, "schoolNotes");
			}

			// course subject notes
			if (course.subjectNotes) {
				var subjectNotesId = courseId + ".subjectNotes";
				var subjectNotesIdDiv = dojo.create("div", { id:subjectNotesId, innerHTML:"<span>Subject " + course.subject + " Notes:</span> " + (course.subjectNotes) }, courseNotesHeaderIdDiv);
				dojo.addClass(subjectNotesIdDiv, "subjectNotes");
			}

			// course core curriculum codes
			if (course.coreCodes && course.coreCodes.length > 0) {
				var coreCodesId = courseId + ".coreCodes";
				var coreCodesIdDiv = dojo.create("div", { id:coreCodesId, innerHTML:"<span>SAS Core Code:</span> " + dojo.map(course.coreCodes, function(item) { return item.description + " (" + item.code + ")"; }).join(", ") }, courseNotesHeaderIdDiv);
				dojo.addClass(coreCodesIdDiv, "coreCodes");
			}

			// course sections
			CourseDisplayUtils.refreshSectionData(course, courseId, courseDataIdDiv);
		},
		// refreshSectionData
		refreshSectionData: function(course, courseId, courseDataIdDiv) {

			var courseSectionsId = courseId + ".courseSections";
			var courseSectionsIdDiv = dojo.create("div", { id:courseSectionsId }, courseDataIdDiv);
			dojo.addClass(courseSectionsIdDiv, "courseSections");
			dojo.addClass(courseSectionsIdDiv, "hidden3");
			
			// sectionHeaders: SEC, INDEX, MEETINGS TIMES/LOCATIONS, TYPE, EXAM CODE, INSTRUCTIONS
			var sectionHeadersId = courseId + ".sectionHeaders";
			var sectionHeadersIdDiv = dojo.create("div", { id:sectionHeadersId }, courseSectionsIdDiv);
			dojo.addClass(sectionHeadersIdDiv, "sectionHeaders");
			
			// sectionHeader: sectionColumnHeader
			var sectionColumnHeader = sectionHeadersId + ".sectionColumnHeader";
			var sectionColumnHeaderDiv = dojo.create("span", { id:sectionColumnHeader, innerHTML:"SEC" }, sectionHeadersIdDiv);
			dojo.addClass(sectionColumnHeaderDiv, "sectionColumnHeader");
						
			// sectionHeader: indexNumberColumnHeader
			var indexNumberColumnHeader = sectionHeadersId + ".indexNumberColumnHeader";
			var indexNumberColumnHeaderDiv = dojo.create("span", { id:indexNumberColumnHeader, innerHTML:"INDEX" }, sectionHeadersIdDiv);
			dojo.addClass(indexNumberColumnHeaderDiv, "indexNumberColumnHeader");
			
			// sectionHeader: meetingTimeColumnHeader
			var meetingTimeColumnHeader = sectionHeadersId + ".meetingTimeColumnHeader";
			var meetingTimeColumnHeaderDiv = dojo.create("span", { id:meetingTimeColumnHeader, innerHTML:"MEETING TIMES / LOCATIONS" }, sectionHeadersIdDiv);
			dojo.addClass(meetingTimeColumnHeaderDiv, "meetingTimeColumnHeader");
			
			// sectionHeader: examCodeColumnHeader
			var examCodeColumnHeader = sectionHeadersId + ".examCode";
			var examCodeColumnHeaderDiv = dojo.create("span", { id:examCodeColumnHeader, innerHTML:"EC" }, sectionHeadersIdDiv);
			dojo.addClass(examCodeColumnHeaderDiv, "examCodeColumnHeader");
			
			// sectionHeader: examCodeColumnHeader tooltip
			new dijit.Tooltip({ connectId:examCodeColumnHeaderDiv, label:"Exam Code", showDelay:0, position:["above", "below", "after", "before"] });
			
			// sectionHeader: instructorColumnHeader
			var instructorColumnHeader = sectionHeadersId + ".instructorColumnHeader";
			var instructorColumnHeaderDiv = dojo.create("span", { id:instructorColumnHeader, innerHTML:"INSTRUCTORS" }, sectionHeadersIdDiv);
			dojo.addClass(instructorColumnHeaderDiv, "instructorColumnHeader");

			// sectionListings
			var sectionListingsId = courseId + ".sectionListings";
			var sectionListingsIdDiv = dojo.create("div", { id:sectionListingsId }, courseSectionsIdDiv);
			dojo.addClass(sectionListingsIdDiv, "sectionListings");

			// loop sections
			dojo.forEach(course.sections, function(section, i) {
				// check for section print flag
				if (!SectionUtils.isSectionPrintFlagEnabled(section)) return;
				// section
				var sectionId = courseId + ".section" + section.number + "." + i;
				var sectionIdDiv = dojo.create("div", { id:sectionId }, sectionListingsIdDiv);
				dojo.addClass(sectionIdDiv, "section");
				// section filters
				if (SectionUtils.isRestrictedSection(section)) {
					dojo.addClass(sectionIdDiv, "sectionStatus_restricted");
				}
				else {
					dojo.addClass(sectionIdDiv, (section.openStatus == true ? "sectionStatus_open" : "sectionStatus_closed"));
				}
				dojo.addClass(sectionIdDiv, "courseType_" + CourseTypeUtils.getCourseType(section));
				// check if section is off campus
				if (CampusUtils.isOffCampus(section.campusCode)) {
					dojo.addClass(sectionIdDiv, "courseType_F");
					// remove traditional campus type
					dojo.removeClass(sectionIdDiv, "courseType_T");
				}
				// section sub-campus filters
				dojo.addClass(sectionIdDiv, CampusUtils.getSubcampusFilters(section));
				// section meeting day and time filters
				dojo.addClass(sectionIdDiv, MeetingUtils.getMeetingDayAndTimeFilters(section));
				// section notes header
				var sectionNotesHeader = sectionId + ".sectionNotesHeader";
				if (section.subtitle 
					|| section.subtopic 
					|| section.sectionNotes 
					|| section.sessionDates 
					|| section.sectionEligibility 
					|| SectionUtils.hasOpenTo(section)
					|| (section.crossListedSections && section.crossListedSections.length > 0) 
					|| (section.comments && section.comments.length > 0) 
					|| (section.specialPermissionAddCodeDescription)) {
					var sectionNotesHeaderDiv = dojo.create("div", { id:sectionNotesHeader }, sectionIdDiv);
					dojo.addClass(sectionNotesHeaderDiv, "sectionNotesHeader");
				}

				// section notes header: subtitle
				if (section.subtitle) {
					var sectionSubtitleId = sectionId + ".sectionSubtitleId";
					var sectionSubtitleIdDiv = dojo.create("div", { id:sectionSubtitleIdDiv, innerHTML:"<span>Section " + section.number + " Subtitle:</span> " + section.subtitle }, sectionNotesHeaderDiv);
					dojo.addClass(sectionSubtitleIdDiv, "sectionSubtitle");
				}

				// section notes header: description
				if (section.subtopic) {
					var sectionDescriptionId = sectionId + ".sectionDescriptionId";
					var sectionDescriptionIdDiv = dojo.create("div", { id:sectionDescriptionIdDiv, innerHTML:"<span>Section " + section.number + " Description:</span> " + section.subtopic }, sectionNotesHeaderDiv);
					dojo.addClass(sectionDescriptionIdDiv, "sectionDescription");
				}

				// section notes header: notes
				if (section.sectionNotes) {
					var sectionNotesId = sectionId + ".sectionNotesId";
					var sectionNotesIdDiv = dojo.create("div", { id:sectionNotesId, innerHTML:"<span>Section " + section.number + " Notes:</span> " + SectionUtils.formatSectionNotes(section.sectionNotes) }, sectionNotesHeaderDiv);
					dojo.addClass(sectionNotesIdDiv, "sectionNotes");
				}

				// section notes header: session dates
				if (section.sessionDates && section.sessionDatePrintIndicator == "Y") {
					var sectionSessionDatesId = sectionId + ".sectionSessionDatesId";
					var sectionSessionDatesIdDiv = dojo.create("div", { id:sectionSessionDatesId, innerHTML:"<span>Section " + section.number + " Session Dates:</span> " + section.sessionDates }, sectionNotesHeaderDiv);
					dojo.addClass(sectionSessionDatesIdDiv, "sectionSessionDates");
				}

				// section notes header: restrictions
				if (section.sectionEligibility) {
					var sectionRestrictionsId = sectionId + ".sectionRestrictionsId";
					var sectionRestrictionsIdDiv = dojo.create("div", { id:sectionRestrictionsId, innerHTML:"<span>Section " + section.number + " Restrictions:</span> " + section.sectionEligibility }, sectionNotesHeaderDiv);
					dojo.addClass(sectionRestrictionsIdDiv, "sectionRestrictions");
				}

				// section notes header: comments
				if (section.comments && section.comments.length > 0) {
					// concat all comments
					var comments = "";
					dojo.forEach(section.comments, function(comment) {
						var description = comment.description;
						if (description) comments += description + ", ";
					});
					if (comments.length >= 2) comments = comments.substr(0, comments.length - 2);
					// create section comments div
					if (dojo.trim(comments).length > 0) {
						var commentsId = sectionId + ".sectionCommentsId";
						var commentsIdDiv = dojo.create("div", { id:commentsId, innerHTML:"<span>Section " + section.number + " Comments:</span> " + comments }, sectionNotesHeaderDiv);
						dojo.addClass(commentsIdDiv, "sectionComments");
					}
				}

				// section notes header: open to
				if (SectionUtils.hasOpenTo(section)) {
					var sectionOpenToId = sectionId + ".sectionOpenToId";
					var sectionOpenToIdDiv = dojo.create("div", { id:sectionOpenToId, innerHTML:"<span>Section " + section.number + " Open To:</span> " + SectionUtils.getOpenTo(section) }, sectionNotesHeaderDiv);
				}

				// section notes header: cross-listed sections
				if (section.crossListedSections && section.crossListedSections.length > 0) {
					var crossListedSectionsId = sectionId + ".sectionCrossListedSectionsId";
					var crossListedSectionsIdDiv = dojo.create("div", { id:crossListedSectionsId, innerHTML:"<span>Section " + section.number + " Cross-Listed With:</span> " + SectionUtils.getCrossListedSections(section) }, sectionNotesHeaderDiv);
					dojo.addClass(crossListedSectionsIdDiv, "sectionCrossListedSections");
				}

				// section notes header: special permission code descriptions
				if (section.specialPermissionAddCodeDescription) {
					var specId = sectionId + ".sectionSpecialPermissionCodesId";
					var specIdDiv = dojo.create("div", { id:specId, innerHTML:"<span>Section " + section.number + " Special Permission:</span> " + section.specialPermissionAddCodeDescription.toUpperCase() }, sectionNotesHeaderDiv);
					dojo.addClass(specIdDiv, "sectionSpecialPermissionCodes");
				}

				// section data ************************
				var sectionDataId = sectionId + ".sectionData";
				var sectionDataIdDiv = dojo.create("div", { id:sectionDataId }, sectionIdDiv);
				dojo.addClass(sectionDataIdDiv, "sectionData");

				// section data: number
				var sectionDataNumber = sectionDataId + ".number";
				var sectionDataNumberItem = dojo.create("span", { id:sectionDataNumber, style:"vertical-align:middle;width:20px;line-height:25px;" }, sectionDataIdDiv);
				dojo.addClass(sectionDataNumberItem, "sectionDataNumber");

				var sectionDataNumberSpan = sectionDataNumber + ".span";
				var sectionDataNumberSpanItem = dojo.create("span", { id:sectionDataNumberSpan, innerHTML:section.number }, sectionDataNumberItem);
				// add color to section number box
				if (SectionUtils.isRestrictedSection(section)) {
					dojo.addClass(sectionDataNumberSpanItem, "sectionrestricted");
					// create tooltip for restricted sections
					new dijit.Tooltip({ connectId:[sectionDataNumberSpanItem], label:"This is a restricted section", showDelay:0, position:["above", "below", "before", "after"] });
				}
				else {
					dojo.addClass(sectionDataNumberSpanItem, (section.openStatus == true ? "sectionopen" : "sectionclosed"));
				}

				// section data: index
				var sectionIndexNumber = dojo.create("span", { innerHTML:section.index, style:"margin-left:9px;text-align:center;vertical-align:middle;width:50px" }, sectionDataIdDiv);
				dojo.addClass(sectionIndexNumber, "sectionIndexNumber");

				// section data: meeting times
				var sectionDataMeetingTimes = sectionDataId + ".meetingTimes";
				var sectionDataMeetingTimesItem = dojo.create("span", { id:sectionDataMeetingTimes, style:"vertical-align:middle;width:320px" }, sectionDataIdDiv);
				dojo.addClass(sectionDataMeetingTimesItem, "sectionMeetingTimes");

				var sectionDataMeetingTimesDivOuter = sectionDataMeetingTimes + ".divOuter";
				var sectionDataMeetingTimesDivOuterItem = dojo.create("div", { id:sectionDataMeetingTimesDivOuter }, sectionDataMeetingTimesItem);
				dojo.addClass(sectionDataMeetingTimesDivOuterItem, "sectionMeetingTimesDiv");

				// check for null meeting times, add text "Hours By Arrangement"
				if (dojo.every(section.meetingTimes, function(time) { return !time.startTime && !time.endTime; })) {
					var sectionDataMeetingTimesDivInner = sectionDataMeetingTimes + ".divInner";
					var sectionDataMeetingTimesDivInnerItem = dojo.create("div", { id:sectionDataMeetingTimesDivInner, innerHTML:"Hours By Arrangement" }, sectionDataMeetingTimesDivOuterItem);
					dojo.addClass(sectionDataMeetingTimesDivInnerItem, "hoursByArrangement");
				}
				else {

					// sort section meeting times
					var meetingTimes = MeetingUtils.sortMeetingTimes(section.meetingTimes);
					dojo.forEach(meetingTimes, function(time, i) {
						var sectionDataMeetingTimesDivInner = sectionDataMeetingTimes + ".divInner" + i;

						// meeting time of by arrangement, within other valid meeting times
						if (time.baClassHours == "B") {
							var sectionDataMeetingTimesDivInnerItem = dojo.create("div", { id:sectionDataMeetingTimesDivInner, style:"line-height:18px;" }, sectionDataMeetingTimesDivOuterItem);
							// add text "Hours By Arrangement" and campus
							var s1;
							if (dojo.isIE && dojo.isIE < 8) {
								s1 = "<span style='width:155px;font-style:italic;color:#777;'>Hours By Arrangement</span>";
							}
							else {
								s1 = "<span style='width:178px;font-style:italic;color:#777;'>Hours By Arrangement</span>";
							}
							var s2 = "<span style='width:30px;'>" + CampusUtils.getSubcampusCode(time) + "</span>";
							dojo.byId(sectionDataMeetingTimesDivInnerItem).innerHTML = s1 + s2;
							return;
						}

						var sectionDataMeetingTimesDivInnerItem = dojo.create("div", { id:sectionDataMeetingTimesDivInner, style:"line-height:18px;" }, sectionDataMeetingTimesDivOuterItem);
						if (time.meetingDay != null) {
							// check if recitation
							var isRecitation = MeetingUtils.isRecitation(time);
							// day name
							var sectionDataMeetingTimesDayName = sectionDataMeetingTimesDivInner + ".dayName";
							var dayName = MeetingUtils.getMeetingDayName(time.meetingDay);
							var sectionDataMeetingTimesDayNameItem = dojo.create("span", { id:sectionDataMeetingTimesDayName, style:"width:55px;", innerHTML:dayName }, sectionDataMeetingTimesDivInnerItem);
							dojo.addClass(sectionDataMeetingTimesDayNameItem, "meetingTimeDay");

							if (isRecitation) {
								dojo.addClass(sectionDataMeetingTimesDayNameItem, "recitation");
								// create tooltip for recitation
								new dijit.Tooltip({ connectId:[sectionDataMeetingTimesDayNameItem], label:dayName + ": RECITATION", showDelay:0, position:["above", "below", "before", "after"] });
							}

							// hours
							var sectionDataMeetingTimesHours = sectionDataMeetingTimesDivInner + ".hours";
							var sectionDataMeetingTimesHoursItem = dojo.create("span", { id:sectionDataMeetingTimesHours, style:"width:103px;", innerHTML:MeetingUtils.getMeetingHours(time) }, sectionDataMeetingTimesDivInnerItem);
							dojo.addClass(sectionDataMeetingTimesHoursItem, "meetingTimeHours");
							if (isRecitation) dojo.addClass(sectionDataMeetingTimesHoursItem, "recitation");

							// campus
							var sectionDataMeetingTimesCampus = sectionDataMeetingTimesDivInner + ".campus";
							var campusCode = CampusUtils.getSubcampusCode(time);
							var sectionDataMeetingTimesCampusItem = dojo.create("span", { id:sectionDataMeetingTimesCampus, style:"width:30px;", innerHTML:campusCode }, sectionDataMeetingTimesDivInnerItem);
							dojo.addClass(sectionDataMeetingTimesCampusItem, "meetingTimeCampus");
							if (isRecitation) dojo.addClass(sectionDataMeetingTimesCampusItem, "recitation");

							// campus tooltip
							new dijit.Tooltip({ connectId:[sectionDataMeetingTimesCampusItem], label:CampusUtils.getSubcampusDescription(campusCode), showDelay:0, position:["above", "below", "before", "after"] });

							// building
							var buildingAndRoom = BuildingUtils.getBuildingAndRoom(time);
							var sectionDataMeetingTimesBuilding = sectionDataMeetingTimesDivInner + ".building";
							
							// building map link
							var buildingMapLink = LocationUtils.getBuildingMapLink(buildingAndRoom);
							var buildingAndRoomHtml = buildingMapLink ? "<a href='" + buildingMapLink + "' target='_blank'>" + buildingAndRoom + "</a>" : buildingAndRoom;
							var sectionDataMeetingTimesBuildingItem = dojo.create("span", { id:sectionDataMeetingTimesBuilding, innerHTML:buildingAndRoomHtml }, sectionDataMeetingTimesDivInnerItem);
							dojo.addClass(sectionDataMeetingTimesBuildingItem, "meetingTimeBuildingAndRoom");
							if (isRecitation) dojo.addClass(sectionDataMeetingTimesBuildingItem, "recitation");

							// building tooltip
							var buildingTooltipLabel = BuildingUtils.getBuildingDescription(buildingAndRoom);
							if (buildingTooltipLabel) new dijit.Tooltip({ connectId:[sectionDataMeetingTimesBuildingItem], label:buildingTooltipLabel, showDelay:0, position:["above", "below", "before", "after"] });
						}
					});
				}

				// section data: exam code
				var examCode = section.examCode;
				var sectionDataExamCodeId = sectionDataId + ".examCode";
				var sectionDataExamCodeIdItem = dojo.create("span", { id:sectionDataExamCodeId, innerHTML:examCode }, sectionDataIdDiv);
				dojo.addClass(sectionDataExamCodeIdItem, "examCode");
				new dijit.Tooltip({ connectId:sectionDataExamCodeIdItem, label:"Exam Code: " + examCode + " <font color='#777777'>- " + ExamUtils.getExamCodeDescription(examCode) + "</font>", showDelay:0, position:["above", "below", "after", "before"] });

				// section data: instructors
				var _instructors = dojo.create("span", { innerHTML:InstructorUtils.getInstructorsHtml(section) }, sectionDataIdDiv);
				dojo.addClass(_instructors, "instructors");

				// section data: books
				var booksSpan = dojo.create("span", { innerHTML:BookUtils.getBooksLink(course, section) }, sectionDataIdDiv);
				dojo.addClass(booksSpan, "books");

				if (SectionUtils.isRestrictedSection(section)) {
					// section data: request enrollment
					var requestLink = RegisterUtils.getRequestLink(Constants.selectedSemester, section);
					var requestLinkHtml = "<a href='" + requestLink + "' target='_blank'>Request Enrollment</a>";
					var requestLinkSpan = dojo.create("span", { innerHTML:requestLinkHtml }, sectionDataIdDiv);
					dojo.addClass(requestLinkSpan, "requestLinkSpan");
				}
				else {
					// section data: register
					var registerLink = RegisterUtils.getRegisterLink(Constants.selectedSemester, section);
					var registerLinkHtml;
					if (Constants.isIframe) {
						registerLinkHtml = "<a href='" + registerLink + "' target='_top'>Register</a>";
					}
					else {
						registerLinkHtml = "<a href='" + registerLink + "' target='_blank'>Register</a>";
					}
					var registerSpan = dojo.create("span", { innerHTML:registerLinkHtml }, sectionDataIdDiv);
					dojo.addClass(registerSpan, "register");
				}
			});
		}
	};
})();

// CourseLoadingUtils
var CourseLoadingUtils = (function() {
	return {
		// publishCoursesFound
		publishCoursesFound: function(json) {
			var length = 0;
			if (json) {
				dojo.forEach(json, function(course) {
					if (SectionUtils.isAllSectionsPrintFlagDisabled(course.sections)) return;
					length++;
				});
			}
			Constants.coursesFound = length;
			dojo.publish("notificationWidget", [{ message: "<div id='numCoursesFoundDiv'><span id='numCoursesFoundDivSpan'>" + length + "</span> course" + (length == 1 ? "" : "s") + " found.</div>", duration:4200 }]);
		},
		// publishSearching
		publishSearching: function(keyword) {
			dojo.publish("notificationWidget", [{ message:"<div id='searchingCoursesDiv'>Searching ..</div>" }]);
		}
	};
})();

// CourseSectionUtils
var CourseSectionUtils = (function() {
	return {
		buildCourseSectionId: function(courseId, section, j) {
			var tmp = [];
			tmp.push(courseId);
			tmp.push(".section");
			tmp.push(section.number);
			tmp.push(".");
			tmp.push(j);
			return tmp.join("");
		},
		isSectionOpen: function(section) {
			return section.openStatus == true;
		},
		isSectionClosed: function(section) {
			return section.openStatus == false;
		},
		isTypeTraditional: function(section) {
			if (CourseSectionUtils.isTypeOnline(section)) return false;
			if (CourseSectionUtils.isTypeHybrid(section)) return false;
			return true;
		},
		isTypeOnline: function(section) {
			var online = false;
			dojo.forEach(section.meetingTimes, function(time) {
				if (time.meetingModeCode && time.meetingModeCode == "90") online = true;
			});
			return online;
		},
		isTypeHybrid: function(section) {
			var hybrid = false;
			dojo.forEach(section.meetingTimes, function(time) {
				if (time.meetingModeCode && time.meetingModeCode == "91") hybrid = true;
			});
			return hybrid;
		},
		isTypeOffCampus: function(section) {
			return section.campusCode != "NB" && section.campusCode != "NK" && section.campusCode != "CM";
		},
		isMondayMorning: function(section) {
			var filter = false;
			dojo.forEach(section.meetingTimes, function(time) {
				if (MeetingTimeUtils.isDayMonday(time) && MeetingTimeUtils.isTimeMorning(time)) filter = true;
			});
			return filter;
		},
		isMondayAfternoon: function(section) {
			var filter = false;
			dojo.forEach(section.meetingTimes, function(time) {
				if (MeetingTimeUtils.isDayMonday(time) && MeetingTimeUtils.isTimeAfternoon(time)) filter = true;
			});
			return filter;
		},
		isMondayEvening: function(section) {
			var filter = false;
			dojo.forEach(section.meetingTimes, function(time) {
				if (MeetingTimeUtils.isDayMonday(time) && MeetingTimeUtils.isTimeEvening(time)) filter = true;
			});
			return filter;
		},
		isTuesdayMorning: function(section) {
			var filter = false;
			dojo.forEach(section.meetingTimes, function(time) {
				if (MeetingTimeUtils.isDayTuesday(time) && MeetingTimeUtils.isTimeMorning(time)) filter = true;
			});
			return filter;
		},
		isTuesdayAfternoon: function(section) {
			var filter = false;
			dojo.forEach(section.meetingTimes, function(time) {
				if (MeetingTimeUtils.isDayTuesday(time) && MeetingTimeUtils.isTimeAfternoon(time)) filter = true;
			});
			return filter;
		},
		isTuesdayEvening: function(section) {
			var filter = false;
			dojo.forEach(section.meetingTimes, function(time) {
				if (MeetingTimeUtils.isDayTuesday(time) && MeetingTimeUtils.isTimeEvening(time)) filter = true;
			});
			return filter;
		},
		isWednesdayMorning: function(section) {
			var filter = false;
			dojo.forEach(section.meetingTimes, function(time) {
				if (MeetingTimeUtils.isDayWednesday(time) && MeetingTimeUtils.isTimeMorning(time)) filter = true;
			});
			return filter;
		},
		isWednesdayAfternoon: function(section) {
			var filter = false;
			dojo.forEach(section.meetingTimes, function(time) {
				if (MeetingTimeUtils.isDayWednesday(time) && MeetingTimeUtils.isTimeAfternoon(time)) filter = true;
			});
			return filter;
		},
		isWednesdayEvening: function(section) {
			var filter = false;
			dojo.forEach(section.meetingTimes, function(time) {
				if (MeetingTimeUtils.isDayWednesday(time) && MeetingTimeUtils.isTimeEvening(time)) filter = true;
			});
			return filter;
		},
		isThursdayMorning: function(section) {
			var filter = false;
			dojo.forEach(section.meetingTimes, function(time) {
				if (MeetingTimeUtils.isDayThursday(time) && MeetingTimeUtils.isTimeMorning(time)) filter = true;
			});
			return filter;
		},
		isThursdayAfternoon: function(section) {
			var filter = false;
			dojo.forEach(section.meetingTimes, function(time) {
				if (MeetingTimeUtils.isDayThursday(time) && MeetingTimeUtils.isTimeAfternoon(time)) filter = true;
			});
			return filter;
		},
		isThursdayEvening: function(section) {
			var filter = false;
			dojo.forEach(section.meetingTimes, function(time) {
				if (MeetingTimeUtils.isDayThursday(time) && MeetingTimeUtils.isTimeEvening(time)) filter = true;
			});
			return filter;
		},
		isFridayMorning: function(section) {
			var filter = false;
			dojo.forEach(section.meetingTimes, function(time) {
				if (MeetingTimeUtils.isDayFriday(time) && MeetingTimeUtils.isTimeMorning(time)) filter = true;
			});
			return filter;
		},
		isFridayAfternoon: function(section) {
			var filter = false;
			dojo.forEach(section.meetingTimes, function(time) {
				if (MeetingTimeUtils.isDayFriday(time) && MeetingTimeUtils.isTimeAfternoon(time)) filter = true;
			});
			return filter;
		},
		isFridayEvening: function(section) {
			var filter = false;
			dojo.forEach(section.meetingTimes, function(time) {
				if (MeetingTimeUtils.isDayFriday(time) && MeetingTimeUtils.isTimeEvening(time)) filter = true;
			});
			return filter;
		},
		isSaturdayMorning: function(section) {
			var filter = false;
			dojo.forEach(section.meetingTimes, function(time) {
				if (MeetingTimeUtils.isDaySaturday(time) && MeetingTimeUtils.isTimeMorning(time)) filter = true;
			});
			return filter;
		},
		isSaturdayAfternoon: function(section) {
			var filter = false;
			dojo.forEach(section.meetingTimes, function(time) {
				if (MeetingTimeUtils.isDaySaturday(time) && MeetingTimeUtils.isTimeAfternoon(time)) filter = true;
			});
			return filter;
		},
		isSaturdayEvening: function(section) {
			var filter = false;
			dojo.forEach(section.meetingTimes, function(time) {
				if (MeetingTimeUtils.isDaySaturday(time) && MeetingTimeUtils.isTimeEvening(time)) filter = true;
			});
			return filter;
		},
		isSundayMorning: function(section) {
			var filter = false;
			dojo.forEach(section.meetingTimes, function(time) {
				if (MeetingTimeUtils.isDaySunday(time) && MeetingTimeUtils.isTimeMorning(time)) filter = true;
			});
			return filter;
		},
		isSundayAfternoon: function(section) {
			var filter = false;
			dojo.forEach(section.meetingTimes, function(time) {
				if (MeetingTimeUtils.isDaySunday(time) && MeetingTimeUtils.isTimeAfternoon(time)) filter = true;
			});
			return filter;
		},
		isSundayEvening: function(section) {
			var filter = false;
			dojo.forEach(section.meetingTimes, function(time) {
				if (MeetingTimeUtils.isDaySunday(time) && MeetingTimeUtils.isTimeEvening(time)) filter = true;
			});
			return filter;
		}
	};
})();

// CourseService
var CourseService = (function() {
	// attrs
	var prereqDialog;
	// buildSubjects
	var buildSubjects = function(json) {
		var subjects = [];
		dojo.forEach(json, function(item /* { code,description } */) {
			// attach label attr
			//item.label = item.code + ":" + item.description;
			item.label = item.description + " (" + item.code + ")";
			subjects.push(item);
		});
		// save to Constants
		Constants.currentSubjects = subjects;
	};
	// buildSubjectsAndRefreshDropdown
	var buildSubjectsAndRefreshDropdown = function(json) {
		buildSubjects(json);
		// reset filteringselect widget
		SingleSubjectSearchUtils.initSubjectFilteringSelectWidget();
	};
	return {
		// initSchoolSubjects
		initSchoolSubjects: function() {
			dojo.xhrGet({ url:"schoolSubjects.json", content:{ year:Constants.getSelectedYear(), term:Constants.getSelectedTerm(), school:Constants.selectedSchool }, handleAs:"json", error:Constants.ERROR, sync:true,
				load:function(json) {
					buildSubjects(json);
				}
			});
		},
		// initSubjects
		initSubjects: function() {
			if (Constants.isOnlineCourses()) {
				// get onlineSubjects.json
				dojo.xhrGet({ url:"onlineSubjects.json", content:{ year:Constants.getSelectedYear(), term:Constants.getSelectedTerm(), level:Constants.selectedLevel }, handleAs:"json", error:Constants.ERROR, sync:true,
					load:function(json) {
						buildSubjectsAndRefreshDropdown(json);
					}
				});
				return;
			}
			else {
				// get subjects.json
				dojo.xhrGet({ url:"subjects.json", content:{ semester:Constants.selectedSemester, campus:Constants.selectedCampus, level:Constants.selectedLevel }, handleAs:"json", error:Constants.ERROR, sync:true,
					load:function(json) {
						buildSubjectsAndRefreshDropdown(json);
					}
				});
				return;
			}
		},
		// toggleExpandAllCourses
		toggleExpandAllCourses: function() {
			var isAllCoursesHidden = dojo.every(dojo.query(".courseSections"), function(item) { return dojo.hasClass(item, "hidden3"); });
			if (isAllCoursesHidden) {
				dojo.query(".courseNotesHeader").forEach(function(item) { dojo.toggleClass(item, "hidden3", false); });
				dojo.query(".courseSections").forEach(function(item) { dojo.toggleClass(item, "hidden3", false); });
				dojo.query(".courseExpandIcon").forEach(function(item) {
					// get image tag
					var image = dojo.byId(item).childNodes[0];
					// update image src
					dojo.attr(image, "src", "images/down_arrow.png");
					// update image position class
					dojo.addClass(item, "expanded"); 
				});
			}
			else {
				dojo.query(".courseNotesHeader").forEach(function(item) { dojo.toggleClass(item, "hidden3", true); });
				dojo.query(".courseSections").forEach(function(item) { dojo.toggleClass(item, "hidden3", true); });
				dojo.query(".courseExpandIcon").forEach(function(item) { 
					// get image tag
					var image = dojo.byId(item).childNodes[0];
					// update image src
					dojo.attr(image, "src", "images/right_arrow.png");
					// update image position class
					dojo.removeClass(item, "expanded");  
				});
			}
		},
		// openSynopsis
		openSynopsis: function(url) {
			window.open(url);
		},
		// expandOrCollapseCourse
		expandOrCollapseCourse: function(courseId) {
			// toggle course notes header
			var courseNotesHeaderId = courseId + ".courseNotesHeader";
			dojo.toggleClass(courseNotesHeaderId, "hidden3");
			// toggle course sections
			var courseSectionsId = courseId + ".courseSections";
			dojo.toggleClass(courseSectionsId, "hidden3");
			// toggle arrow icon
			var courseExpandIconId = courseId + ".courseExpandIcon";
			if (dojo.hasClass(courseExpandIconId, "expanded")) {
				dojo.removeClass(courseExpandIconId, "expanded");
				dojo.byId(courseExpandIconId).innerHTML = "<img src='images/right_arrow.png' onclick='CourseService.expandOrCollapseCourse(\"" + courseId + "\");' />";
			}
			else {
				dojo.addClass(courseExpandIconId, "expanded");
				dojo.byId(courseExpandIconId).innerHTML = "<img src='images/down_arrow.png' onclick='CourseService.expandOrCollapseCourse(\"" + courseId + "\");' />";
			}
		},
		// loadPrereqDialog
		loadPrereqDialog: function(el) {
			var id = dojo.attr(el, "id");
			var content = PrereqUtils.getPrereqTooltipContent(id);
			var title = "<span style='font-family:georgia,arial;'>Prereqs for " + id.substr(0, id.indexOf(".")) + "</span>";
			var style = "color:#000;font-family:tahoma,arial;font-size:11px;";
			// check if dialog is already created
			if (prereqDialog) {
				prereqDialog.set("title", title);
				prereqDialog.set("content", content);
				prereqDialog.set("style", style);
			}
			else {
				prereqDialog = new dijit.Dialog({ title:title, style:style, content:content });
			}
			prereqDialog.show();
		},
		// reset
		reset: function() {
			CourseDisplayUtils.clearAllCourses();
			dojo.empty("showHideAllDiv");
			MultiSubjectCourseUtils.reset();
		}
	}
})();

// CourseTypeUtils
var CourseTypeUtils = (function() {
	return {
		// getCourseType
		getCourseType: function(section) {
			// T = traditional, H = hybrid, O = online, 7 = 7 week
			var type = "T";
			dojo.forEach(section.meetingTimes, function(time) {
				if (time.meetingModeCode != null && time.meetingModeCode == "91") type = "H";
				else if (time.meetingModeCode != null && time.meetingModeCode == "90") type = "O";
			});
			/*
			dojo.forEach(section.comments, function(comment) {
				if (comment != null && (comment.code == "12" || comment.code == "13")) type = "7";
			});
			*/
			return type;
		},
		// getCourseTypeName
		getCourseTypeName: function(type) {
			return Constants.COURSETYPES[type];
		}
	};
})();

// CourseUtils
var CourseUtils = (function() {
	return {
		buildCourseId: function(course, i) {
			var tmp = [];
			tmp.push(course.offeringUnitCode);
			tmp.push(":");
			tmp.push(course.subject);
			tmp.push(":");
			tmp.push(course.courseNumber);
			tmp.push(".");
			tmp.push(i);
			return tmp.join("");
		},
		hasExpandedTitle: function(course) {
			return course.expandedTitle && dojo.trim(course.expandedTitle).length > 0;
		},
		getTitleExpandedPreferred: function(course) {
			return CourseUtils.hasExpandedTitle(course) ? dojo.trim(course.expandedTitle) : dojo.trim(course.title);
		},
		getCourse: function(courseId) {
			return Constants.coursesMap[courseId];
		}
	};
})();

// ExamUtils
var ExamUtils = (function() {
	return {
		// getExamCodeDescription
		getExamCodeDescription: function(code) {
			var name;
			dojo.forEach(Constants.EXAMCODES, function(lookup) {
				if (lookup.code == code) name = lookup.name;
			});
			return name ? name : "N/A";
		}
	};
})();

// FilterService
var FilterService = (function() {
	return {
		// appendCourseCreditFilters
		appendCourseCreditFilters: function(items) {
			if (items.length == 0) {
				dojo.byId("courseCreditFilters").style.display = "none";
				return;
			}
			dojo.empty("courseCreditFiltersList");
			var html = "";
			dojo.forEach(items, function(item) {
				html += "<li><input class='filter' onclick='FilterService.filterCourses(\".courseCredit_" + item.code + "\", this)' type='checkbox' checked> <span class='cursortext' onclick='FormService.clickChildNode(this.parentNode);'>" + item.name + "</span></li>";
				if (dojo.isIE != 7) html += "<br>";
			});
			// update inner html with course credits
			dojo.byId("courseCreditFiltersList").innerHTML = html;
			// unhide course credits filter section
			dojo.byId("courseCreditFilters").style.display = "";
		},
		// appendCoreCurriculumFilters
		appendCoreCurriculumFilters: function(items) {
			// check for no core codes in all courses
			if (items.length == 0) {
				dojo.byId("corecurriculumfilters").style.display = "none";
				return;
			}
			dojo.empty("corecurriculumfilterslist");
			var html = "";
			dojo.forEach(items, function(item) {
				html += "<li><input class='filter coreCodeFilter' coreCodeValue='" + item.code + "' onclick='FilterService.filterCoreCodes()' type='checkbox' checked> <span class='cursortext' onclick='FormService.clickChildNode(this.parentNode);'>" + item.name + " (" + item.code + ")</span></li>";
				if (dojo.isIE != 7) html += "<br>";
			});
			// add default N/A filter
			html += "<li><input class='filter coreCodeFilter' coreCodeValue='NA' onclick='FilterService.filterCoreCodes()' type='checkbox' checked> <span class='cursortext' id='corecodena' onclick='FormService.clickChildNode(this.parentNode);' style='font-style:italic;'>N/A</span></li>";
			if (dojo.isIE != 7) html += "<br>";
			// update inner html with core codes
			dojo.byId("corecurriculumfilterslist").innerHTML = html;
			// unhide core codes filter section
			dojo.byId("corecurriculumfilters").style.display = "";
			// create tooltip for NA core code
			new dijit.Tooltip({ connectId:"corecodena", label: "<span id='corecodena_tooltip'>All courses without a <b>SAS Core Code</b> association</span>", showDelay:0, position: ["above", "below"] });
		},
		// appendSchoolFilters
		appendSchoolFilters: function(schools) {
			dojo.empty("school_filters_list");
			var html = "";
			dojo.forEach(schools, function(school) {
				html += "<li><input class='filter' onclick='FilterService.filterCourses(\".school_" + school.code + "\", this)' type='checkbox' checked> <span class='cursortext' onclick='FormService.clickChildNode(this.parentNode);'>" + (SchoolUtils.getSchoolLabel(school.code)) + "</span></li>";
				if (dojo.isIE != 7) html += "<br>";
			});
			dojo.byId("school_filters_list").innerHTML = html;
		},
		// appendSubjectFilters
		appendSubjectFilters: function(subjects) {
			dojo.empty("subject_filters_list");
			var html = "";
			dojo.forEach(subjects.split(","), function(subject) {
				if (!subject || dojo.trim(subject).length == 0) return;
				html += "<li><input class='filter' onclick='FilterService.filterSubject(\"" + subject + "\", this);' type='checkbox' checked> <span class='cursortext' onclick='FormService.clickChildNode(this.parentNode);'>" + SubjectUtils.getSubjectDescription(subject) + " (" + subject + ")</span></li>";
				if (dojo.isIE != 7) html += "<br>";
			});
			dojo.byId("subject_filters_list").innerHTML = html;
		},
		// appendKeywordSubjectFilters
		appendKeywordSubjectFilters: function(subjects) {
			dojo.empty("subject_filters_list");
			var html = "";
			dojo.forEach(subjects, function(item) {
				html += "<li><input class='filter' onclick='FilterService.filterSubject(\"" + item.code + "\", this);' type='checkbox' checked> <span class='cursortext' onclick='FormService.clickChildNode(this.parentNode);'>" + SubjectUtils.getSubjectDescription(item.code) + " (" + item.code + ")</span></li>";
				if (dojo.isIE != 7) html += "<br>";
			});
			dojo.byId("subject_filters_list").innerHTML = html;
		},
		// checkAllFilters
		checkAllFilters: function() {
			dojo.query(".filter").forEach(function(item) { item.checked = true; });
		},
		// resetAllFilters
		resetAllFilters: function() {
			// unhide everything
			dojo.query(".hidden2").forEach(function(item) { dojo.toggleClass(item, "hidden2", false);	});
			dojo.query(".hidden3").forEach(function(item) { dojo.toggleClass(item, "hidden3", false);	});
			dojo.query(".hidden4").forEach(function(item) { dojo.toggleClass(item, "hidden4", false);	});
			// hide subheaders
			dojo.query(".courseNotesHeader").forEach(function(item) { dojo.toggleClass(item, "hidden3", true); });
			// hide sections
			dojo.query(".courseSections").forEach(function(item) { dojo.toggleClass(item, "hidden3", true); });
			// update arrow icon
			dojo.query(".courseExpandIcon").forEach(function(item) {
				// get image tag
				var image = dojo.byId(item).childNodes[0];
				// update image src
				dojo.attr(image, "src", "images/right_arrow.png");
				// update image position class
				dojo.removeClass(item, "expanded");
			});
			FilterService.checkAllFilters();
		},
		// filterCoreCodes
		filterCoreCodes: function() {
			// hide courses first
			dojo.query(".coreCodeFilter").forEach(function(item) {
				if (!item.checked) {
					// get filter code
					var code = dojo.attr(item, "coreCodeValue");
					// get courses
					dojo.query(".courseItem").forEach(function(courseItem) {
						if (dojo.hasClass(courseItem, "coreCode_" + code)) {
							dojo.toggleClass(courseItem, "hidden4", true);
						}
					});
				}
			});
			// show courses second
			dojo.query(".coreCodeFilter").forEach(function(item) {
				if (item.checked) {
					// get filter code
					var code = dojo.attr(item, "coreCodeValue");
					// get courses
					dojo.query(".courseItem").forEach(function(courseItem) {
						if (dojo.hasClass(courseItem, "coreCode_" + code)) {
							dojo.toggleClass(courseItem, "hidden4", false);
						}
					});
				}
			});
		},
		// filterSections
		filterSections: function() {
			if (SearchUtils.isSchoolSearchType()) {
				SchoolCourseFilterUtils.filterSections();
				return;
			}
			// unhide all sections
			dojo.query(".hidden2").forEach(function(item) { dojo.removeClass(item, "hidden2"); });
			// query section filters
			dojo.query("input[sectionFilter]").forEach(function(item) {
				// process unchecked boxes only
				if (item.checked == false) {
					// sectionFilter attr matches className of dom item to hide (ie. courseType_O)
					var filter = dojo.attr(item, "sectionFilter");
					// hide sections that match filter
					dojo.query(filter).forEach(function(item2) { dojo.addClass(item2, "hidden2"); });
				}
			});
			// hide courses that have all sections filtered
			FilterService.filterCoursesWithAllSectionsFiltered();
		},
		// filterCoursesWithAllSectionsFiltered
		filterCoursesWithAllSectionsFiltered: function() {
			dojo.query(".sectionListings").forEach(function(listings) {
				var hidden = dojo.every(listings.childNodes, function(item) { return dojo.hasClass(item, "hidden2"); });
				// listings.id is like '01:198:111.1.sectionListings'
				var listingsId = listings.id.substr(0, listings.id.lastIndexOf("."));
				dojo.toggleClass(dojo.byId(listingsId), "hidden2", hidden);
			});
		},
		// filterSectionsDay
		filterSectionsDay: function(day /* M,T,W,TH,F,S,U */) {
			var morning = dojo.byId(day + "_morning");
			var afternoon = dojo.byId(day + "_afternoon");
			var evening = dojo.byId(day + "_evening");
			// if all unchecked, do click()
			if (!morning.checked && !afternoon.checked && !evening.checked) {
				morning.click();
				afternoon.click();
				evening.click();
			}
			else {
				// uncheck those that are checked
				if (morning.checked) morning.click();
				if (afternoon.checked) afternoon.click();
				if (evening.checked) evening.click();
			}
		},
		// filterSectionsTime
		filterSectionsTime: function(time /* morning,afternoon,evening */) {
			var monday = dojo.byId("M_" + time);
			var tuesday = dojo.byId("T_" + time);
			var wednesday = dojo.byId("W_" + time);
			var thursday = dojo.byId("TH_" + time);
			var friday = dojo.byId("F_" + time);
			var saturday = dojo.byId("S_" + time);
			var sunday = dojo.byId("U_" + time);
			// if all unchecked, do click()
			if (!monday.checked && !tuesday.checked && !wednesday.checked && !thursday.checked && !friday.checked && !saturday.checked && !sunday.checked) {
				monday.click();
				tuesday.click();
				wednesday.click();
				thursday.click();
				friday.click();
				saturday.click();
				sunday.click();
			}
			else {
				// uncheck those that are checked
				if (monday.checked) monday.click();
				if (tuesday.checked) tuesday.click();
				if (wednesday.checked) wednesday.click();
				if (thursday.checked) thursday.click();
				if (friday.checked) friday.click();
				if (saturday.checked) saturday.click();
				if (sunday.checked) sunday.click();
			}
		},
		// filterCourses
		filterCourses: function(className, e) {
			className = className.replace(/\./g, "_");
			className = "." + className.substr(1);
			dojo.query(className).forEach(function(item) { dojo.toggleClass(item, "hidden3", !e.checked); });
		},
		// filterSubject
		filterSubject: function(code, e) {
			dojo.query(".subject_" + code).forEach(function(item) { dojo.toggleClass(item, "hidden3", !e.checked); });
		},
		// showOrHideFiltersBox
		showOrHideFiltersBox: function() {
			dojo.toggleClass(dojo.byId("main_box"), "hidden");
			dojo.toggleClass(dojo.byId("showFiltersBoxDiv"), "hidden");
		},
		// initLevelOfStudyFilters
		initLevelOfStudyFilters: function() {
			if (SearchUtils.isLocationSearchType() || SearchUtils.isUnitCourseSearchType() || SearchUtils.isCoreCodeSearchType()) {
				// unhide level of study filters based on user selection
				dojo.forEach(Constants.selectedLevel.split(","), function(code) {
					var id = "levelofstudy_" + code;
					if (dojo.byId(id)) dojo.byId(id).style.display = "";
				});
			}
			else if (SearchUtils.isSchoolSearchType()) {
				dojo.byId("levelofstudy_U").style.display = "";
				dojo.byId("levelofstudy_G").style.display = "";
			}
		},
		// initNewBrunswickSubCampusFilters
		initNewBrunswickSubCampusFilters: function() {
			if (dojo.some(Constants.selectedCampus.split(","), function(campus) { return campus == "NB"; })) { 
				dojo.byId("nb_subcampus_div").style.display = ""; 
			}
		},
		// initCourseTypesFilters
		initCourseTypesFilters: function() {
			if (Constants.isOnlineCourses()) {
				dojo.byId("coursetypes_span1").style["display"] = "none";
			}
			else {
				dojo.byId("coursetypes_span1").style["display"] = "";
			}
		},
		// initDayAndTimeFilters
		initDayAndTimeFilters: function() {
			if (Constants.isOnlineCourses()) {
				dojo.byId("dayAndTimeFilters").style["display"] = "none";
			}
			else {
				dojo.byId("dayAndTimeFilters").style["display"] = "";
			}
		}
	}
})();

// FilterUtils
var FilterUtils = (function() {
	// resetLevelOfStudyFilters
	function resetLevelOfStudyFilters() { 
		for (var code in Constants.LEVELS) dojo.byId("levelofstudy_" + code).style.display = "none"; 
	}
	// resetNewBrunswickSubCampusFilters
	function resetNewBrunswickSubCampusFilters() {
		dojo.byId("nb_subcampus_div").style.display = "none"; 
	}
	// resetSchoolFilters
	function resetSchoolFilters() {
		dojo.byId("school_filters_div").style.display = "none"; 
	}
	// resetSubjectFilters
	function resetSubjectFilters() {
		dojo.byId("subject_filters_div").style.display = "none"; 
	}
	// resetCoreCurriculumFilters
	function resetCoreCurriculumFilters() {
		dojo.byId("corecurriculumfilters").style.display = "none";
	}
	// resetCourseCreditFilters
	function resetCourseCreditFilters() {
		dojo.byId("courseCreditFilters").style.display = "none";
	}
	return {
		// reset
		reset: function() {
			// reset level filters
			resetLevelOfStudyFilters();
			// reset NB subcampus filters
			resetNewBrunswickSubCampusFilters();
			// reset school filters
			resetSchoolFilters();
			// reset subject filters
			resetSubjectFilters();
			// reset core curriculum filters
			resetCoreCurriculumFilters();
			// reset course credit filters
			resetCourseCreditFilters();
		},
		// disableFilters
		disableFilters: function() {
			dojo.query(".filter").forEach(function(item) { item.disabled = true; });
		},
		// enableFilters
		enableFilters: function() {
			dojo.query(".filter").forEach(function(item) { item.disabled = false; });
		}
	};
})();

// FormService
var FormService = (function() {
	var submitLocationSearch = function() {
		// validate form values
		if (!StringUtils.hasLength(Constants.selectedSemester)) { // 1		
			if (!StringUtils.hasLength(Constants.selectedCampus)) { // 1 + 2
				if (!StringUtils.hasLength(Constants.selectedLevel)) { // 1 + 2 + 3
					alert("Please select a term, location, and level of study.");
					return;
				}
				alert("Please select a term and location.");
				return;
			}
			else if (!StringUtils.hasLength(Constants.selectedLevel)) { // 1 + 3
				alert("Please select a term and level of study.");
				return;
			}
			alert("Please select a term.");
			return;
		}
		if (!StringUtils.hasLength(Constants.selectedCampus)) { // 2
			if (!StringUtils.hasLength(Constants.selectedLevel)) { // 2 + 3
				alert("Please select a location and level of study.");
				return;
			}
			alert("Please select a location.");
			return;
		}
		if (!StringUtils.hasLength(Constants.selectedLevel)) { // 3
			alert("Please select a level of study.");
			return;
		}
		// update history
		var fragment = "subjects?semester=" + Constants.selectedSemester + "&campus=" + Constants.selectedCampus + "&level=" + Constants.selectedLevel;
		HistoryUtils.addHistory(fragment);
		ContentUtils.loadContent(fragment);
	};
	var submitSchoolSearch = function() {
		// validate form values
		if (!StringUtils.hasLength(Constants.selectedSemester)) { // 1
			if (!StringUtils.hasLength(Constants.selectedSchool)) { // 2
				alert("Please select a term and school.");
				return;
			}
			alert("Please select a term.");
			return;
		}
		if (!StringUtils.hasLength(Constants.selectedSchool)) { // 2
			alert("Please select a school.");
			return;
		}
		// update history
		var fragment = "schools?semester=" + Constants.selectedSemester + "&school=" + Constants.selectedSchool;
		HistoryUtils.addHistory(fragment);
		ContentUtils.loadContent(fragment);
	};
	var _selectSearchTab = function(id, isSelected) {
		if (isSelected) {
			dojo.removeClass(dojo.byId(id), "unselected");
			dojo.addClass(dojo.byId(id), "selected");
		}
		else {
			dojo.addClass(dojo.byId(id), "unselected");
			dojo.removeClass(dojo.byId(id), "selected");
		}
	};
	return {		
		// submit
		submit: function() {
			// get submitted form values
			FormUtils.captureFormValues();
			// check for location search
			if (Constants.searchType == Constants.LOCATION_SEARCH) submitLocationSearch();
			// check for school search
			else if (Constants.searchType == Constants.SCHOOL_SEARCH) submitSchoolSearch();
			// hide messages
			MessageUtils.hideMessages();
		},
		// initPage
		initPage: function() {
			// init semesters
			FormUtils.initSemesterForm();
			// clear all checkboxes
			dojo.query("input[type=checkbox]").forEach(function(item) { dojo.byId(item).checked = false; });
			// init schools
			FormUtils.initSchools();
			// init buildings
			FormUtils.initBuildings();
			// init core codes
			FormUtils.initCoreCodes();
		},
		// togglePreviousSemesters
		togglePreviousSemesters: function() {
			dojo.toggleClass("previousSemesters", "hidden");
			dojo.byId("previousSemestersLabel").innerHTML = dojo.hasClass("previousSemesters", "hidden") ? "[+] Show Previous Terms" : "[ - ] Hide Previous Terms";
		},
		// toggleShowOffCampus
		toggleShowOffCampus: function() {
			dojo.toggleClass("locationsOffCampusList", "hidden");
			dojo.byId("locationsOffCampusListLabel").innerHTML = dojo.hasClass("locationsOffCampusList", "hidden") ? "&gt; Show Off-Campus Centers" : "&gt; Hide Off-Campus Centers";
		},
		// updateHouseIcon
		updateHouseIcon: function() {
			// check if winter
			if (Constants.selectedSemester.charAt(0) == "0") dojo.attr("imageHouseId", "src", "images/house_winter.png");
			else dojo.attr("imageHouseId", "src", "images/house_red.png");
		},
		// reset
		reset: function() {
			// hide search/filters content
			dojo.addClass("searchfilterscontent", "hidden");
			// hide back to top link			
			dojo.addClass("backToTop", "hidden");
			// hide core codes tab
			dojo.byId("core_code_search_id").style["display"] = "none";
			// reset FilterService
			FilterUtils.reset();
			// reset CourseService
			CourseService.reset();
			// unhide form, disclaimers, old version of soc link
			dojo.removeClass("termlocationlevelform", "hidden");
			dojo.removeClass("disclaimers", "hidden");
			// show messages
			MessageUtils.showSystemMessage();
		},
		// createCampusTooltipsInTitle
		createCampusTooltipsInTitle: function() {
			dojo.query(".campus").forEach(function(item) {
				new dijit.Tooltip({ connectId: item.id, label: "<span class='tooltip_campus'>" + dojo.attr(item, "campusName") + "</span>", showDelay:0, position: ["above", "below"] });
			});
		},
		// getters
		getSemesterTermValueTwoDigitYear: function() { return Constants.selectedSemester.substr(0, 1) + Constants.selectedSemester.substr(3, 5); },
		// clickChildNode
		clickChildNode: function(el) { el.childNodes[0].click(); },			
		// selectSubjectTab
		selectSubjectTab: function() {
			_selectSearchTab("subject_search_id", true);
			_selectSearchTab("keyword_search_id", false);
			_selectSearchTab("core_code_search_id", false);
			// hide keyword input/options
			dojo.addClass("keyword_search_options_id", "hidden");
			dojo.addClass("keywordSearchFilterDiv", "hidden");
			// hide core code search dropdown
			dojo.addClass("core_code_search_options_id", "hidden");
			// unhide subject search dropdown/link
			dojo.removeClass("subject_search_options_id", "hidden");
			SingleSubjectSearchUtils.initSubjectFilteringSelectWidget();
			// clear core code search label
			CoreCodeSearchUtils.clearFilteringSelectLabel();
		},
		// selectKeywordTab
		selectKeywordTab: function() {
			_selectSearchTab("keyword_search_id", true);
			_selectSearchTab("subject_search_id", false);
			_selectSearchTab("core_code_search_id", false);
			// hide subject search dropdown/link
			dojo.addClass("subject_search_options_id", "hidden");
			// hide core code search dropdown
			dojo.addClass("core_code_search_options_id", "hidden");
			// unhide keyword input/options
			dojo.removeClass("keyword_search_options_id", "hidden");
			dojo.removeClass("keywordSearchFilterDiv", "hidden");
			// clear core code search label
			CoreCodeSearchUtils.clearFilteringSelectLabel();
			// clear single subject search label
			SingleSubjectSearchUtils.clearSubjectFilteringSelectLabel();
		},
		// selectCoreCodeTab
		selectCoreCodeTab: function() {
			_selectSearchTab("core_code_search_id", true);
			_selectSearchTab("subject_search_id", false);
			_selectSearchTab("keyword_search_id", false);
			// hide subject search dropdown/link
			dojo.addClass("subject_search_options_id", "hidden");
			// hide keyword input/options
			dojo.addClass("keyword_search_options_id", "hidden");
			dojo.addClass("keywordSearchFilterDiv", "hidden");
			// unhide core code search dropdown
			dojo.removeClass("core_code_search_options_id", "hidden");
			CoreCodeSearchUtils.initCoreCodeFilteringSelectWidget();
			// clear single subject search label
			SingleSubjectSearchUtils.clearSubjectFilteringSelectLabel();
		}
	};
})();

// FormUtils
var FormUtils = (function() {
	return {
		// captureFormValues
		captureFormValues: function() {
			// get semester
			var tmp = "";
			dojo.query("input").forEach(function(item) { if (dojo.attr(dojo.byId(item), "name") == "semester" && item.checked) tmp = item.value; });
			Constants.selectedSemester = tmp;
			// get campus
			tmp = "";
			dojo.query("input").forEach(function(item) { if (dojo.attr(dojo.byId(item), "name") == "campus" && item.checked) tmp += item.value + ","; });
			tmp = tmp.substr(0, tmp.length - 1);
			Constants.selectedCampus = tmp;
			// get level
			tmp = "";
			dojo.query("input").forEach(function(item) { if (dojo.attr(dojo.byId(item), "name") == "level" && item.checked) tmp += item.value + ","; });
			tmp = tmp.substr(0, tmp.length - 1);
			Constants.selectedLevel = tmp;
			// get school
			Constants.selectedSchool = dojo.byId("schoolSearchSelect").value;
		},
		// initCoreCodes
		initCoreCodes: function() {
			dojo.xhrGet({ url:"coreCodes.json", handleAs:"json", sync:true, error:Constants.ERROR, load:function(json) { Constants.CORE_CODES = json; }});
		},
		// initBuildings
		initBuildings: function() {
			dojo.xhrGet({ url:"buildings.json", handleAs:"json", sync:true, error:Constants.ERROR, load:function(json) { Constants.BUILDINGS = json; }});
		},
		// initSchools
		initSchools: function() {
			dojo.xhrGet({ url:"schools.json", handleAs:"json", sync:true, error:Constants.ERROR, load:function(json) { Constants.SCHOOLS = json; }});
			var html = "&nbsp;<select style='font-family:tahoma;font-size:11px;' id='schoolSearchSelect'>";
			dojo.forEach(Constants.SCHOOLS, function(item) {
				html += "<option value='" + item.code + "'>" + SchoolUtils.getSchoolLabel(item.code) + "</option>";
			});
			html += "</select>";
			dojo.byId("schoolList").innerHTML = html;
		},
		// initSemesterForm
		initSemesterForm: function() {
			if (Constants.isIframe) {
				// parse term parameter
				var term = UrlParserUtils.parseTerm();
				// convert to semester object { name:"", code:"" }
				var semester = SemesterUtils.parseSemester(term);
				// add to array, copy to Constants.SEMESTERS
				var semesters = [];
				semesters.push(semester);
				Constants.SEMESTERS = semesters;
				// build semester html
				var html = "<li><input type='radio' id='semester' name='semester' value='" + semester.code + "' checked> <span class='cursortext' onclick='FormService.clickChildNode(this.parentNode);'>" + semester.name + "</span></li>";
				dojo.byId("currentSemesters").innerHTML += html;
				return;
			}
			var semesters = SemesterUtils.getSemesters();
			var currentSemestersHtml = "";
			var previousSemestersHtml = "";
			// current semesters
			dojo.forEach(semesters.slice(0, 3), function(item) {
				var html = "<li><input type='radio' id='semester' name='semester' value='" + item.code + "'> <span class='cursortext' onclick='FormService.clickChildNode(this.parentNode);'>" + item.name + "</span></li>";
				currentSemestersHtml += html;
			});
			// previous semesters
			dojo.forEach(semesters.slice(3), function(item) {
				var html = "<li><input type='radio' id='semester' name='semester' value='" + item.code + "'> <span class='cursortext' onclick='FormService.clickChildNode(this.parentNode);'>" + item.name + "</span></li>";
				previousSemestersHtml += html;
			});
			dojo.byId("currentSemesters").innerHTML = currentSemestersHtml;
			dojo.byId("previousSemesters").innerHTML = previousSemestersHtml;
		},
		// selectCurrentTerm
		selectCurrentTerm: function() {
			dojo.byId("currentSemesters").childNodes[0].childNodes[0].click();
		},
		// selectCurrentLevels
		selectCurrentLevels: function() {
			dojo.query(".level").forEach(function(item) { dojo.byId(item).click(); });
		}
	};
})();

// HistoryUtils
var HistoryUtils = (function() {
	var State = function(fragment) {
		this.changeUrl = fragment;
	};
	dojo.extend(State, {
		back: function() {
			ContentUtils.loadContent(this.changeUrl);
		},
		forward: function() {
			ContentUtils.loadContent(this.changeUrl);
		}
	});
	return {
		// getFragment
		getFragment: function() {
			var parts = window.location.href.split("#");
			return parts.length == 2 ? parts[1] : "home";
		},
		// initHistory
		initHistory: function(fragment) {
			dojo.back.setInitialState(new State(fragment));
		},
		// addHistory
		addHistory: function(fragment) {
			dojo.back.addToHistory(new State(fragment));
		}
	};
})();

// IframeUtils
var IframeUtils = (function() {
	return {
		handleIframe: function() {
			var url = window.location.href;
			Constants.isIframe = url.indexOf("iframe=true") != -1;
			Constants.iframeUrl = url;
			if (!Constants.isIframe) {
				dojo.byId("banner").style.display = "";
				dojo.byId("appname").style.display = "";
				dojo.byId("previousSemestersLabel").style.display = "";
				dojo.byId("footer").style.display = "";
			}
		}
	};
})();

// InstructionUtils
var InstructionUtils = (function() {
	return {
		// showInstructions
		showInstructions: function() {
			dojo.byId("instructions").style.display = "";
		},
		// hideInstructions
		hideInstructions: function() {
			dojo.byId("instructions").style.display = "none";
		}
	};
})();

// InstructorUtils
var InstructorUtils = (function() {
	return {
		// getInstructorsHtml
		getInstructorsHtml: function(section) {
			var instructors = section.instructors;
			if (instructors.length == 0) return "";
			instructors.sort(function(i, j) {
				var result = -1;
				if (i.name > j.name) result = 1;
				return result;
			});
			var html = instructors[0].name;
			for (var i = 1; i < instructors.length; i++) { html += "; " + instructors[i].name; }
			return html;
		}
	};
})();

// KeywordFilterUtils
var KeywordFilterUtils = (function() {
	return {
		// filterMatchWordStart
		filterMatchWordStart: function() {
			// reset to match any
			KeywordFilterUtils.filterMatchAnyPart();
			// query each course
			dojo.query(".metadata").forEach(function(item) {
				// get title, extended title
				var title = MetadataUtils.getCourseTitleFromMetadataItem(item);
				var extendedTitle = MetadataUtils.getCourseExtendedTitleFromMetadataItem(item);
				// check keyword against title
				if (KeywordFilterUtils.isWordStartMatch(Constants.currentKeyword, title)) return;
				// check keyword against extended title
				if (KeywordFilterUtils.isWordStartMatch(Constants.currentKeyword, extendedTitle)) return;
				// doesn't match, hide course
				var id = MetadataUtils.getCourseIdFromMetadataItem(item);
				dojo.addClass(id, "hidden");
			});
		},
		// filterMatchWholeWord
		filterMatchWholeWord: function() {
			// reset to match any
			KeywordFilterUtils.filterMatchAnyPart();
			// query each course
			dojo.query(".metadata").forEach(function(item) {
				// get title, extended title
				var title = MetadataUtils.getCourseTitleFromMetadataItem(item);
				var extendedTitle = MetadataUtils.getCourseExtendedTitleFromMetadataItem(item);
				// check keyword against title
				if (KeywordFilterUtils.isWholeWordMatch(Constants.currentKeyword, title)) return;
				// check keyword against extended title
				if (KeywordFilterUtils.isWholeWordMatch(Constants.currentKeyword, extendedTitle)) return;
				// doesn't match, hide course
				var id = MetadataUtils.getCourseIdFromMetadataItem(item);
				dojo.addClass(id, "hidden");
			});
		},
		// filterMatchAnyPart
		filterMatchAnyPart: function() {
			// unhide all results
			dojo.query(".metadata").forEach(function(item) {
				var id = MetadataUtils.getCourseIdFromMetadataItem(item);
				dojo.removeClass(id, "hidden");
			});
		},
		// isWordStartMatch
		isWordStartMatch: function(keyword, title) {
			// replace ampersands with whitespace
			title = title.replace(/&AMP;/g, " ");
			// replace dashes with whitespace
			title = title.replace(/-/g, " ");
			// split on whitespace
			var tokens = title.split(" ");
			for (var i = 0; i < tokens.length; i++) {
				if (tokens[i].substr(0, keyword.length) == keyword) return true;
			}
			return false;
		},
		// isWholeWordMatch
		isWholeWordMatch: function(keyword, title) {
			// replace ampersands with whitespace
			title = title.replace(/&AMP;/g, " ");
			// replace dashes with whitespace
			title = title.replace(/-/g, " ");
			// split on whitespace
			var tokens = title.split(" ");
			for (var i = 0; i < tokens.length; i++) {
				if (tokens[i] == keyword) return true;
			}
			return false;
		},
		// resetKeywordSearchFilters
		resetKeywordSearchFilters: function() {
			dojo.byId("defaultKeywordSearchFilterId").checked = true;
		}
	};
})();

// LevelUtils
var LevelUtils = (function() {
	return {
		// isLevelSelected
		isLevelSelected: function(level) {
			return dojo.some(Constants.selectedLevel.split(","), function(item) { return item == level; });
		}
	};
})();

// LocationUtils
var LocationUtils = (function() {
	return {
		// getBuildingMapLink
		getBuildingMapLink: function(buildingAndRoom) {
			if (!buildingAndRoom) return null;
			// parse building key (ie, CA, HH, SC, ..)
			var index = buildingAndRoom.indexOf("-");
			if (index < 0) return null;
			var buildingCode = buildingAndRoom.substr(0, index);
			// get building id
			var building = BuildingUtils.getBuildingObject(buildingCode);
			return "http://rumaps.rutgers.edu/bldgnum/" + (building ? building.id : "");
		}
	};
})();

// MeetingUtils
var MeetingUtils = (function() {
	return {
		// sortMeetingTimes
		sortMeetingTimes: function(meetingTimes) {
			return meetingTimes.sort(function(t1, t2) {
				// show by arrangement last
				if (MeetingUtils.isByArrangement(t1)) return 1;
				if (MeetingUtils.isByArrangement(t2)) return -1;
				// show recitations next to last
				if (MeetingUtils.isRecitation(t1)) return 1;
				if (MeetingUtils.isRecitation(t2)) return -1;
				// sort monday thru saturday
				var day1 = Constants.MEETINGDAYRANKINGS[t1.meetingDay];
				var day2 = Constants.MEETINGDAYRANKINGS[t2.meetingDay];
				if (day1 <= day2) return -1;
				return 1;
			});
		},
		// isByArrangement
		isByArrangement: function(meetingTime) {
			return meetingTime.baClassHours == "B";
		},
		// isLecture
		isLecture: function(meetingTime) {
			return meetingTime.meetingModeCode == "02";
		},
		// isRecitation
		isRecitation: function(meetingTime) {
			return meetingTime.meetingModeCode == "03";
		},
		// getMeetingHours
		getMeetingHours: function(time) {
			return MeetingUtils.getMeetingHoursBegin(time) + " - " + MeetingUtils.getMeetingHoursEnd(time);
		},
		// getMeetingHoursBegin
		getMeetingHoursBegin: function(time) {
			var meridian = time.pmCode == "A" ? "AM" : "PM";
			return MeetingUtils.formatMeetingHours(time.startTime) + " " + meridian;
		},
		// getMeetingHoursEnd
		getMeetingHoursEnd: function(time) {
			var meridian = "";
			var starttime = time.startTime;
			var endtime = time.endTime;
			var pmcode = time.pmCode;
			// check pm code
			if (pmcode != "A") meridian = "PM";
			// check like 1pm after 11am
			else if (endtime < starttime) meridian = "PM";
			// check 12pm
			else if (endtime.substr(0, 2) == 12) meridian = "PM";
			// else am
			else meridian = "AM";
			return MeetingUtils.formatMeetingHours(time.endTime) + " " + meridian;
		},
		// formatMeetingHours
		formatMeetingHours: function(time) {
			if (time == null) return "";
			// time = HHmm, convert to H:mm
			if (time.substr(0, 1) == "0") {
				return time.substr(1, 1) + ":" + time.substr(2);
			}
			return time.substr(0, 2) + ":" + time.substr(2);
		},
		// getMeetingDayName
		getMeetingDayName: function(day) {
			var name = Constants.MEETINGDAYNAMES[day];
			return name ? name :  day;
		},
		// getMeetingDayAndTimeFilters
		getMeetingDayAndTimeFilters: function(section) {
			var filters = "";
			dojo.forEach(section.meetingTimes, function(time) {
				var day = time.meetingDay;
				var starttime = time.startTime;
				var endtime = time.endTime;
				if (day != null) {
					var meridian = time.pmCode == "A" ? "AM" : "PM";
					// if AM, add morning filter
					if (meridian == "AM") { 
						filters += day + "_morning "; 
					}
					else {
						// else PM, handle both starttime and endtime, duplicates okay
						if (starttime != null) {
							// startime: determine afternoon vs evening
							var starttimehour = starttime.substring(0, 2);
							if (starttimehour != 12 && starttimehour >= 6) { filters += day + "_evening "; }
							else { filters += day + "_afternoon "; }
						}
						if (endtime != null) {
							// endtime: determine afternoon vs evening
							var endtimehour = endtime.substring(0, 2);
							if (endtimehour != 12 && endtimehour >= 6) { filters += day + "_evening "; }
							else { filters += day +"_afternoon "; }
						}
					}
				}
			});
			return filters;
		}
	};
})();

// MeetingTimeUtils
var MeetingTimeUtils = (function() {
	return {
		isDayMonday: function(time) {
			return time && time.meetingDay == "M";
		},
		isDayTuesday: function(time) {
			return time && time.meetingDay == "T";
		},
		isDayWednesday: function(time) {
			return time && time.meetingDay == "W";
		},
		isDayThursday: function(time) {
			return time && time.meetingDay == "TH";
		},
		isDayFriday: function(time) {
			return time && time.meetingDay == "F";
		},
		isDaySaturday: function(time) {
			return time && time.meetingDay == "S";
		},
		isDaySunday: function(time) {
			return time && time.meetingDay == "U";
		},
		isTimeMorning: function(time) {
			return time && time.pmCode == "A";
		},
		isTimeAfternoon: function(time) {
			if (!time || !time.pmCode) return false;
			if (time.pmCode == "A") {
				var endhour = time.endTime.substring(0, 2);
				if (endhour == 12 || endhour < 6) return true;
			}
			var starthour = time.startTime.substring(0, 2);
			if (starthour == 12 || starthour < 6) return true;
			var endhour = time.endTime.substring(0, 2);
			if (endhour == 12 || endhour < 6) return true;
			return false;
		},
		isTimeEvening: function(time) {
			if (!time || !time.pmCode) return false;
			if (time.pmCode == "A") return false;
			var starthour = time.startTime.substring(0, 2);
			if (starthour != 12 && starthour >= 6) return true;
			var endhour = time.endTime.substring(0, 2);
			if (endhour != 12 && endhour >= 6) return true;
			return false;
		}
	};
})();

// MessageUtils
var MessageUtils = (function() {
	return {
		showSystemMessage: function() {
			// get message
			if (Constants.systemMessage == null) {
				dojo.xhrGet({ url:"current_system_message.json", handleAs:"json", sync:true, error:Constants.ERROR, load:function(json) { 
					if (json) {
						Constants.systemMessage = json; 
					}
					else {
						Constants.systemMessage = "";
					}
				}});
			}
			if (Constants.systemMessage) {
				dojo.byId("message2").innerHTML = "&gt; " + Constants.systemMessage.messageText;
				dojo.byId("message2").style["display"] = "";
			}
		},
		hideMessages: function() {
			dojo.byId("message2").style["display"] = "none";
		}
	};
})();

// MetadataUtils
var MetadataUtils = (function() {
	return {
		// getCourseIdFromMetadataItem
		getCourseIdFromMetadataItem: function(item) {
			return item.id.substr(0, item.id.lastIndexOf("."));
		},
		// getCourseTitleFromMetadataItem
		getCourseTitleFromMetadataItem: function(item) {
			return dojo.byId(item.id + ".title").innerHTML.toUpperCase();
		},
		// getCourseExtendedTitleFromMetadataItem
		getCourseExtendedTitleFromMetadataItem: function(item) {
			return dojo.byId(item.id + ".extendedTitle").innerHTML.toUpperCase();
		}
	};
})();

// MinorUtils
var MinorUtils = (function() {
	return {
		// getMinorDescription
		getMinorDescription: function(code) {
			var description = "";
			for (var i = 0; i < Constants.MINORS.length; i++) {
				if (Constants.MINORS[i].code == code) description = Constants.MINORS[i].description;
			}
			return description.toUpperCase();
		}
	};
})();

// MultiSubjectUtils
var MultiSubjectUtils = (function() {
	return {
		clearAllSelections: function() {
			dojo.query(".subject_multiple").forEach(function(item) {
				dojo.byId(item).checked = false;
			});			
			dojo.byId("multiSubjectCurrentCount").innerHTML = "0";
		}
	};
})();

// PrereqUtils
var PrereqUtils = (function() {
	var tooltips = { };
	return {
		// createPrereqTooltips
		createPrereqTooltips: function() {
			for (var key in tooltips) {
				new dijit.Tooltip({ connectId:[key], label:tooltips[key], showDelay:0, position:["above", "below", "before", "after"] });
			}
		},
		// addPrereqTooltip
		addPrereqTooltip: function(id, notes) {
			// format notes with line breaks
			notes = notes.replace(/ OR /g, "<br><span class='prereqTooltipDelimiter'>OR</span><br>");
			// wrap notes with span tags
			notes = "<span class='prereqTooltip'><center>" + notes + "</center></span>";
			// add to map
			tooltips[id] = notes;
		},
		// getPrereqTooltipContent
		getPrereqTooltipContent: function(id) {
			return tooltips[id];
		}
	};
})();

// RegisterUtils
var RegisterUtils = (function() {
	var crsDomain = null;
	var webregDomain = null;
	var getWebregDomain = function() {
		if (webregDomain != null) return webregDomain;
		var url = window.location.href;
		if (url.indexOf("//dev-") != -1) webregDomain = "https://dev-sims.rutgers.edu";
		else if (url.indexOf("//test-") != -1) webregDomain = "https://test-sims.rutgers.edu";
		else webregDomain = "https://sims.rutgers.edu";
		return webregDomain;
	};
	var getCrsDomain = function() {
		if (crsDomain != null) return crsDomain;
		var url = window.location.href;
		if (url.indexOf("//dev-") != -1) crsDomain = "https://dev-sis.rutgers.edu";
		else if (url.indexOf("//test-") != -1) crsDomain = "https://test-sis.rutgers.edu";
		else crsDomain = "https://sis.rutgers.edu";
		return crsDomain;
	};
	return {
		// getRequestLink
		getRequestLink: function(semester, section) {
			var domain = getCrsDomain();
			return domain + "/crs/request.htm?index=" + section.index + "&semester=" + semester;
		},
		// getRegisterLink
		getRegisterLink: function(semester, section) {
			var domain = getWebregDomain();
			return domain + "/webreg/editSchedule.htm?login=cas&semesterSelection=" + semester + "&indexList=" + section.index;
		}
	};
})();

// SchoolCourseDisplayUtils
var SchoolCourseDisplayUtils = (function() {
	return {
		// displayCourses
		displayCourses: function() {		
			// clear interval
			clearInterval(Constants.refreshCoursesInterval);
			Constants.coursesMap = {};
			// publish loading courses
			SchoolCourseDisplayUtils.publishLoadingCourses();			
			// build courses html
			Constants._buildAndAttachAllCourses = setInterval("SchoolCourseDisplayUtils.buildAndAttachAllCourses()", 200);
			// append course credit filters
			var credits = CourseCreditUtils.getCredits(Constants.courses);
			FilterService.appendCourseCreditFilters(credits);
			// append subject filters
			var subjects = SubjectUtils.getSubjectsFromCourses(Constants.courses);
			FilterService.appendKeywordSubjectFilters(subjects);
			dojo.byId("subject_filters_div").style.display = "";
			// append core curriculum filters
			var cores = CoreCodeUtils.getCoreCodesFromCourses(Constants.courses);
			FilterService.appendCoreCurriculumFilters(cores);
			// reset and enable filters
			FilterService.checkAllFilters();
			FilterUtils.enableFilters();
		},
		// buildAndAttachAllCourses
		buildAndAttachAllCourses: function() {
			clearInterval(Constants._buildAndAttachAllCourses);
			var div = dojo.create("div", { id:"courseDataParent" });
			// append course display
			dojo.forEach(Constants.courses, function(course, i) {
				SchoolCourseDisplayUtils.buildAndAttachCourse(course, i, div); 
			});
			// unhide back to top link
			dojo.removeClass("backToTop", "hidden");
			// append parent div to output
			dojo.byId(Constants.COURSES_OUTPUT_ID).appendChild(div);
			// create prereq tooltips
			PrereqUtils.createPrereqTooltips();
			// publish courses loaded
			SchoolCourseDisplayUtils.publishCoursesLoaded();
		},
		// buildAndAttachCourse
		buildAndAttachCourse: function(course, i, div) {			
			// check if all sections have the printed flag disabled
			if (SectionUtils.isAllSectionsPrintFlagDisabled(course.sections)) return;
			
			// course id
			var courseId = CourseUtils.buildCourseId(course, i);
			Constants.coursesMap[courseId] = course;

			// subject id
			var subjectId = "subject." + course.subject + "." + courseId;
			var subjectIdDiv = dojo.create("div", { id:subjectId }, div);
			dojo.addClass(subjectIdDiv, "subject");
			
			// course list
			var courseListId = subjectId + ".courseList";
			var courseListIdDiv = dojo.create("div", { id:courseListId }, subjectIdDiv);
			
			// course
			var courseIdDiv = dojo.create("div", { id:courseId }, courseListIdDiv);
			
			// course filters
			dojo.addClass(courseIdDiv, "courseItem");
			dojo.addClass(courseIdDiv, "courseLevel_" + course.courseNumber.substr(0, 1) + "00");
			dojo.addClass(courseIdDiv, "school_" + course.offeringUnitCode);
			dojo.addClass(courseIdDiv, "subject_" + course.subject);
			// course filters - core codes
			dojo.forEach(course.coreCodes, function(coreCode) {
				dojo.addClass(courseIdDiv, "coreCode_" + coreCode.code);
			});
			if (course.coreCodes.length == 0) dojo.addClass(courseIdDiv, "coreCode_NA");
			// course filters - course credits
			dojo.addClass(courseIdDiv, CourseCreditUtils.getCourseCreditsFilterClassName(course));

			// course section flag
			dojo.addClass(courseIdDiv, "courseSectionFlag");

			// course info
			var courseInfoId = courseId + ".courseInfo";
			var courseInfoIdDiv = dojo.create("div", { id:courseInfoId }, courseIdDiv);
			dojo.addClass(courseInfoIdDiv, "courseInfo");

			// spacer
			dojo.create("span", { id:"courseSpacer", innerHTML:"&nbsp;" }, courseInfoIdDiv);

			// arrow icon
			var courseExpandIconDiv = dojo.create("span", { id:courseId + ".courseExpandIcon", innerHTML:"<img src='images/right_arrow.png' onclick='SchoolCourseDisplayUtils.toggleCourse(\"" + courseId + "\");' />" }, courseInfoIdDiv);
			dojo.addClass(courseExpandIconDiv, "courseExpandIcon");

			// course numbers
			var courseNum = courseId.substr(0, courseId.lastIndexOf("."));
			var courseIdSpan = dojo.create("span", { id:"courseId", innerHTML:"<span class='highlighttext' onclick='SchoolCourseDisplayUtils.toggleCourse(\"" + courseId + "\");'><span id='courseId." + courseNum + "'>" + courseNum + "</span></span>" }, courseInfoIdDiv);
			// course numbers tooltip
			var courseNumTooltip = "<b>" + course.offeringUnitCode + "</b> [" + SchoolUtils.getSchoolName(course.offeringUnitCode) + "] : <b>" + course.subject + "</b> [" + SubjectUtils.getSubjectDescription(course.subject) + "] : <b>" + course.courseNumber + "</b> [" + CourseUtils.getTitleExpandedPreferred(course) + "]";
			new dijit.Tooltip({ connectId:courseIdSpan, label:courseNumTooltip, showDelay:0, position:["above", "below", "after", "before"] });

			// course title, credits
			var courseTitleAndCreditsId = courseId + ".courseTitleAndCredits";
			var courseTitleAndCreditsIdDiv = dojo.create("span", { id:courseTitleAndCreditsId }, courseInfoIdDiv);
			dojo.addClass(courseTitleAndCreditsIdDiv, "courseTitleAndCredits");

			// course title
			var courseTitleId = courseTitleAndCreditsId + ".courseTitle";
			var hasExpandedTitle = CourseUtils.hasExpandedTitle(course);
			var title = hasExpandedTitle ? dojo.trim(course.expandedTitle) : dojo.trim(course.title);
			var titleMaxLength = 50;
			var titleDisplay = (title.length > titleMaxLength ? title.substring(0, titleMaxLength) + ".." : title);
			titleDisplay = "<span class='highlighttext' onclick='SchoolCourseDisplayUtils.toggleCourse(\"" + courseId + "\");'>" + titleDisplay + "</span>";
			var courseTitleIdDiv = dojo.create("span", { id:courseTitleId, innerHTML:titleDisplay }, courseTitleAndCreditsIdDiv);
			dojo.addClass(courseTitleIdDiv, "courseTitle");
			// course title tooltip
			var titleTooltipDisplay = (title.length > titleMaxLength ? title + " / " + course.title : course.title);
			if (hasExpandedTitle) new dijit.Tooltip({ connectId:courseTitleIdDiv, label:titleTooltipDisplay, showDelay:0, position:["above", "below", "after", "before"] });

			// course credits
			var courseCreditsId = courseTitleAndCreditsId + ".courseCredits";
			var courseCreditsIdDiv = dojo.create("span", { id:courseCreditsId, innerHTML:(course.credits == null ? "Credits by arrangement" : course.credits + " credit(s)") }, courseTitleAndCreditsIdDiv);
			dojo.addClass(courseCreditsIdDiv, "courseCredits");

			// open sections
			var courseOpenSectionsId = courseId + ".courseOpenSections";
			var courseOpenSectionsIdDiv = dojo.create("span", { id:courseOpenSectionsId }, courseInfoIdDiv);
			dojo.addClass(courseOpenSectionsIdDiv, "courseOpenSections");
			var courseOpenSectionsDataId = courseId + ".courseOpenSectionsData";
			var courseOpenSectionsDataIdDiv = dojo.create("span", { id:courseOpenSectionsDataId, innerHTML:"Sections: " }, courseOpenSectionsIdDiv);
			var courseOpenSectionsNumeratorId = courseOpenSectionsId + ".courseOpenSectionsNumerator";
			var courseOpenSectionsNumeratorIdDiv = dojo.create("span", { id:courseOpenSectionsNumeratorId, innerHTML:SectionUtils.getOpenSections(course) }, courseOpenSectionsDataIdDiv);
			dojo.addClass(courseOpenSectionsNumeratorIdDiv, "courseOpenSectionsNumerator");
			var courseOpenSectionsDenominatorId = courseOpenSectionsId + ".courseOpenSectionsDenominator";
			var courseOpenSectionsDenominatorIdDiv = dojo.create("span", { id:courseOpenSectionsDenominatorId, innerHTML:"&nbsp;/&nbsp;" + SectionUtils.getTotalSections(course) }, courseOpenSectionsDataIdDiv);
			dojo.addClass(courseOpenSectionsDenominatorIdDiv, "courseOpenSectionsDenominator");
			// open sections tooltip
			new dijit.Tooltip({ connectId:[courseOpenSectionsDataIdDiv], label:("Open Sections: <span style='color:#060;font-weight:bold;'>" + SectionUtils.getOpenSections(course) + "</span> / Total Sections: " + SectionUtils.getTotalSections(course)), showDelay:0, position:["above", "below", "before", "after"] });

			// course prereqs
			var coursePrereqId = courseId + ".prereq";
			var prereqNotes = course.preReqNotes ? dojo.trim(course.preReqNotes) : null;
			var coursePrereqIdDiv = dojo.create("span", { id:coursePrereqId, innerHTML:(prereqNotes ? "<span id='" + coursePrereqId + "' onclick='CourseService.loadPrereqDialog(this);'>Prereqs</span>" : "<span class='hiddenPrereqsLink'>Prereqs</span>") }, courseInfoIdDiv);
			dojo.addClass(coursePrereqIdDiv, "prereq");
			// course prereqs tooltip
			if (course.preReqNotes != null) PrereqUtils.addPrereqTooltip(coursePrereqId, course.preReqNotes);

			// course synopsis
			var courseSynopsisId = courseId + ".synopsis";
			var synopsisUrl = course.synopsisUrl ? dojo.trim(course.synopsisUrl) : null;
			var courseSynopsisIdDiv = dojo.create("span", { id:courseSynopsisId, innerHTML:(synopsisUrl ? "<span onclick='CourseService.openSynopsis(\"" + synopsisUrl + "\");' class='synopsis'>Synopsis</span>" : "") }, courseInfoIdDiv);
			dojo.addClass(courseSynopsisIdDiv, "synopsis");

			// course data
			var courseDataId = courseId + ".courseData";
			var courseDataIdDiv = dojo.create("div", { id:courseDataId }, courseIdDiv);
			dojo.addClass(courseDataIdDiv, "courseData");

			// course notes header: course description, course notes, school notes
			var courseNotesHeaderId = courseId + ".courseNotesHeader";
			var courseNotesHeaderIdDiv = dojo.create("div", { id:courseNotesHeaderId }, courseDataIdDiv);
			dojo.addClass(courseNotesHeaderIdDiv, "courseNotesHeader");
			dojo.addClass(courseNotesHeaderIdDiv, "hidden3");

			// course description
			if (course.courseDescription) {
				var courseDescriptionId = courseId + "courseDescription";
				var courseDescriptionIdDiv = dojo.create("div", { id:courseDescriptionId, innerHTML:"<span>Course Description:</span> " + (course.courseDescription) }, courseNotesHeaderIdDiv);
				dojo.addClass(courseDescriptionIdDiv, "courseDescription");
			}
			// course notes
			if (course.courseNotes) {
				var courseNotesId = courseId + "courseNotes";
				var courseNotesIdDiv = dojo.create("div", { id:courseNotesId, innerHTML:"<span>Course Notes:</span> " + (course.courseNotes) }, courseNotesHeaderIdDiv);
				dojo.addClass(courseNotesIdDiv, "courseNotes");
			}
			// course supplement code
			if (course.supplementCode && dojo.trim(course.supplementCode)) {
				var courseSupplementCodeId = courseId + ".supplementCode";
				var courseSupplementCodeIdDiv = dojo.create("div", { id:courseSupplementCodeId, innerHTML:"<span>Course Supplement Code:</span> " + (course.supplementCode) }, courseNotesHeaderIdDiv);
				dojo.addClass(courseSupplementCodeIdDiv, "supplementCode");
			}
			// course school/unit notes
			if (course.unitNotes) {
				var schoolNotesId = courseId + "schoolNotes";
				var schoolNotesIdDiv = dojo.create("div", { id:schoolNotesId, innerHTML:"<span>School Notes:</span> " + (course.unitNotes) }, courseNotesHeaderIdDiv);
				dojo.addClass(schoolNotesIdDiv, "schoolNotes");
			}
			// course subject notes
			if (course.subjectNotes) {
				var subjectNotesId = courseId + ".subjectNotes";
				var subjectNotesIdDiv = dojo.create("div", { id:subjectNotesId, innerHTML:"<span>Subject " + course.subject + " Notes:</span> " + (course.subjectNotes) }, courseNotesHeaderIdDiv);
				dojo.addClass(subjectNotesIdDiv, "subjectNotes");
			}
			// course core curriculum codes
			if (course.coreCodes && course.coreCodes.length > 0) {
				var coreCodesId = courseId + ".coreCodes";
				var coreCodesIdDiv = dojo.create("div", { id:coreCodesId, innerHTML:"<span>SAS Core Code:</span> " + dojo.map(course.coreCodes, function(item) { return item.description + " (" + item.code + ")"; }).join(", ") }, courseNotesHeaderIdDiv);
				dojo.addClass(coreCodesIdDiv, "coreCodes");
			}
		},
		// publishLoadingCourses
		publishLoadingCourses: function() {
			dojo.publish("notificationWidget", [{ message:"<div id='searchingCoursesDiv'>Loading courses ..</div>" }]);
		},
		// publishCoursesLoaded
		publishCoursesLoaded: function() {
			dojo.publish("notificationWidget", [{ message:"<div id='searchingCoursesDiv'>Courses loaded.</div>" }]);
		},
		// publishExpandingCourses
		publishExpandingCourses: function() {
			dojo.publish("notificationWidget", [{ message:"<div id='searchingCoursesDiv'>Expanding courses ..</div>" }]);
		},
		// publishCoursesExpanded
		publishCoursesExpanded: function() {
			dojo.publish("notificationWidget", [{ message:"<div id='searchingCoursesDiv'>Courses expanded.</div>" }]);
		},
		// refreshSubjectTitle
		refreshSubjectTitle: function(title) {
			var id = "subjectTitle";
			dojo.destroy(id);
			dojo.create("h4", { id:id }, Constants.COURSES_OUTPUT_ID);
			// add subject title, plus minus to expand/collapse all
			var span = dojo.create("span", { innerHTML:"<u>+</u>&nbsp;&nbsp;" + title }, id);
			dojo.addClass(dojo.byId(span), "cursortext");
			// add click to expand/collapse all function
			dojo.connect(dojo.byId(id), "onclick", null, SchoolCourseDisplayUtils.toggleAllCourses);
			// add subject title tooltip > click to expand/collapse
			new dijit.Tooltip({ connectId:id, label:"<span id='subjectTitleTooltip'>Expand/Collapse All " + Constants.coursesFound + " Courses</span>", showDelay:0, position:["above","below","after"] });
		},
		// toggleCourse
		toggleCourse: function(courseId) {
			if (Constants.coursesIntervalMap[courseId]) {
				clearInterval(Constants.coursesIntervalMap[courseId]);
				delete Constants.coursesIntervalMap[courseId];
			}
			if (Constants._publishExpandingCourses && Object.size(Constants.coursesIntervalMap) == 0) {
				SchoolCourseDisplayUtils.publishCoursesExpanded();
				Constants._publishExpandingCourses = false;
			}
			// build ids
			var iconId = dojo.byId(courseId + ".courseExpandIcon");
			var courseNotesHeaderId = courseId + ".courseNotesHeader";
			var courseSectionsId = courseId + ".courseSections";
			// collapse course
			if (dojo.hasClass(iconId, "expanded")) {
				// update icon
				dojo.byId(iconId).innerHTML = "<img src='images/right_arrow.png' onclick='SchoolCourseDisplayUtils.toggleCourse(\"" + courseId + "\");' />";
			}
			// expand course
			else {
				// check to build course sections html
				if (dojo.hasClass(courseId, "courseSectionFlag")) {
					dojo.removeClass(courseId, "courseSectionFlag");
					// generate course sections html
					var course = CourseUtils.getCourse(courseId);
					CourseDisplayUtils.refreshSectionData(course, courseId, courseId + ".courseData");
					// hide filtered sections
					dojo.forEach(course.sections, function(section, j) {
						if (section._filtered == true) {
							var sectionId = CourseSectionUtils.buildCourseSectionId(courseId, section, j);
							//console.log("sectionId: " + sectionId);
							if (dojo.byId(sectionId) != null) dojo.addClass(dojo.byId(sectionId), "hidden2");
						}
					});
		
				}
				// update icon
				dojo.byId(iconId).innerHTML = "<img src='images/down_arrow.png' onclick='SchoolCourseDisplayUtils.toggleCourse(\"" + courseId + "\");' />";
			}
			dojo.toggleClass(iconId, "expanded");
			dojo.toggleClass(courseNotesHeaderId, "hidden3");
			dojo.toggleClass(courseSectionsId, "hidden3");
		},
		// toggleAllCourses
		toggleAllCourses: function() {
			var isAllCoursesHidden = dojo.every(dojo.query(".courseSections"), function(item) { return dojo.hasClass(item, "hidden3"); });
			// expand all courses
			if (isAllCoursesHidden) {
				SchoolCourseDisplayUtils.publishExpandingCourses();
				Constants._publishExpandingCourses = true;
				var i  = 0;
				for (var courseId in Constants.coursesMap) {
					if (dojo.byId(courseId) == null) alert("null: " + courseId);
					// skip if course is hidden
					if (dojo.hasClass(dojo.byId(courseId), "hidden3")) continue;
					// create interval
					var interval = setInterval("SchoolCourseDisplayUtils.toggleCourse('" + courseId + "')", (300 + (i * 15)));
					Constants.coursesIntervalMap[courseId] = interval;
					i++;
				}
			}
			// collapse all courses
			else {
				dojo.query(".courseNotesHeader").forEach(function(item) { dojo.toggleClass(item, "hidden3", true); });
				dojo.query(".courseSections").forEach(function(item) { dojo.toggleClass(item, "hidden3", true); });
				dojo.query(".courseExpandIcon").forEach(function(item) { 
					// get image tag
					var image = dojo.byId(item).childNodes[0];
					// update image src
					dojo.attr(image, "src", "images/right_arrow.png");
					// update image position class
					dojo.removeClass(item, "expanded");  
				});
			}
		}
	};
})();

// SchoolCourseFilterUtils
var SchoolCourseFilterUtils = (function() {
	return {
		filterSections: function() {
			// mark filtered sections
			dojo.forEach(Constants.courses, function(course, i) {
				var courseId = CourseUtils.buildCourseId(course, i);
				//console.log("filterSections.courseId: " + courseId);
				dojo.forEach(course.sections, function(section) {					
					// printed
					if (section.printed == "N") {
						section._filtered = true;
						return;
					}
					// reset
					section._filtered = false;
					// status: open
					if (!dojo.byId("sectionFilter_openStatus").checked && CourseSectionUtils.isSectionOpen(section)) section._filtered = true;
					// status: closed
					else if (!dojo.byId("sectionFilter_closedStatus").checked && CourseSectionUtils.isSectionClosed(section)) section._filtered = true;
					// type: traditional
					else if (!dojo.byId("sectionFilter_typeTraditional").checked && CourseSectionUtils.isTypeTraditional(section)) section._filtered = true;
					// type: online
					else if (!dojo.byId("sectionFilter_typeOnline").checked && CourseSectionUtils.isTypeOnline(section)) section._filtered = true;
					// type: hybrid
					else if (!dojo.byId("sectionFilter_typeHybrid").checked && CourseSectionUtils.isTypeHybrid(section)) section._filtered = true;
					// type: off-campus
					else if (!dojo.byId("sectionFilter_typeOffCampus").checked && CourseSectionUtils.isTypeOffCampus(section)) section._filtered = true;
					// monday
					else if (!dojo.byId("M_morning").checked && CourseSectionUtils.isMondayMorning(section)) section._filtered = true;
					else if (!dojo.byId("M_afternoon").checked && CourseSectionUtils.isMondayAfternoon(section)) section._filtered = true;
					else if (!dojo.byId("M_evening").checked && CourseSectionUtils.isMondayEvening(section)) section._filtered = true;
					// tuesday
					else if (!dojo.byId("T_morning").checked && CourseSectionUtils.isTuesdayMorning(section)) section._filtered = true;
					else if (!dojo.byId("T_afternoon").checked && CourseSectionUtils.isTuesdayAfternoon(section)) section._filtered = true;
					else if (!dojo.byId("T_evening").checked && CourseSectionUtils.isTuesdayEvening(section)) section._filtered = true;
					// wednesday
					else if (!dojo.byId("W_morning").checked && CourseSectionUtils.isWednesdayMorning(section)) section._filtered = true;
					else if (!dojo.byId("W_afternoon").checked && CourseSectionUtils.isWednesdayAfternoon(section)) section._filtered = true;
					else if (!dojo.byId("W_evening").checked && CourseSectionUtils.isWednesdayEvening(section)) section._filtered = true;
					// thursday
					else if (!dojo.byId("TH_morning").checked && CourseSectionUtils.isThursdayMorning(section)) section._filtered = true;
					else if (!dojo.byId("TH_afternoon").checked && CourseSectionUtils.isThursdayAfternoon(section)) section._filtered = true;
					else if (!dojo.byId("TH_evening").checked && CourseSectionUtils.isThursdayEvening(section)) section._filtered = true;
					// friday
					else if (!dojo.byId("F_morning").checked && CourseSectionUtils.isFridayMorning(section)) section._filtered = true;
					else if (!dojo.byId("F_afternoon").checked && CourseSectionUtils.isFridayAfternoon(section)) section._filtered = true;
					else if (!dojo.byId("F_evening").checked && CourseSectionUtils.isFridayEvening(section)) section._filtered = true;
					// saturday
					else if (!dojo.byId("S_morning").checked && CourseSectionUtils.isSaturdayMorning(section)) section._filtered = true;
					else if (!dojo.byId("S_afternoon").checked && CourseSectionUtils.isSaturdayAfternoon(section)) section._filtered = true;
					else if (!dojo.byId("S_evening").checked && CourseSectionUtils.isSaturdayEvening(section)) section._filtered = true;
					// sunday
					else if (!dojo.byId("U_morning").checked && CourseSectionUtils.isSundayMorning(section)) section._filtered = true;
					else if (!dojo.byId("U_afternoon").checked && CourseSectionUtils.isSundayAfternoon(section)) section._filtered = true;
					else if (!dojo.byId("U_evening").checked && CourseSectionUtils.isSundayEvening(section)) section._filtered = true;
				});
				// make sure course html exists
				if (dojo.byId(courseId) == null) return;
				// check if all sections filtered, then hide course
				if (dojo.every(course.sections, function(section) { return section._filtered == true; })) {
					dojo.toggleClass(courseId, "hidden3", true);
				}
				else {
					dojo.toggleClass(courseId, "hidden3", false);						
					// check if filtered sections are currently displayed, then hide section
					dojo.forEach(course.sections, function(section, j) {
						var sectionId = CourseSectionUtils.buildCourseSectionId(courseId, section, j);
						//console.log("filterSections.sectionId: " + sectionId);
						if (dojo.byId(sectionId) != null) {
							dojo.toggleClass(sectionId, "hidden2", section._filtered == true);
						}
					});
				}
			});
		}
	};
})();

// SchoolUtils
var SchoolUtils = (function() {
	return {
		getCampusCode: function(unit) {
			var tmp = "";
			dojo.forEach(Constants.SCHOOLS, function(item) {
				if (item.code == unit) tmp = item.homeCampus;
			});
			return tmp;
		},
		getSchoolLabel: function(schoolCode) {
			var school = SchoolUtils.getSchoolItem(schoolCode);
			return school.description + " (" + school.campus + ") - " + school.code;
		},
		getSchoolItem: function(code) {
			var tmp;
			dojo.forEach(Constants.SCHOOLS, function(item) {
				if (item.code == code) tmp = item;
			});
			return tmp;
		},
		getSchoolsFromCourses: function(courses) {
			// add school codes to a map for uniqueness
			var codesMap = { };
			dojo.forEach(courses, function(item) {
				var code = item.offeringUnitCode;
				codesMap[code] = code;
			});
			// convert codes to items, add to list
			var schoolsList = [];
			for (var code in codesMap) {
				var school = SchoolUtils.getSchoolItem(code);
				schoolsList.push(school);
			};
			schoolsList.sort(function(a, b) {
				var result = -1;
				if (a.description > b.description) result = 1;
				return result;
			});
			return schoolsList;
		},
		// getSchoolName
		getSchoolName: function(code) {
			var name;
			dojo.forEach(Constants.SCHOOLS, function(school) {
				if (school.code == code) name = school.description;
			});
			return name ? name : Constants.NA;
		}
	}
})();

// SearchTabUtils
var SearchTabUtils = (function() {
	return {
		initSchoolSearchTab: function() {
			// hide keyword tab
			dojo.byId("keyword_search_id").style.display = "none";
			// replace subject text with school
			dojo.byId("subject_search_id").innerHTML = "School";
			// hide multiple subjects link
			dojo.byId("multi_subject_link").style.display = "none";
			dojo.byId("multi_subject_link_placeholder").style.display = "";
		},
		initLocationSearchTab: function() {
			// unhide keyword tab
			dojo.byId("keyword_search_id").style.display = "";
			// unhide core code tab
			if (CampusUtils.isCampusSelected("NB") && LevelUtils.isLevelSelected("U")) {
				dojo.byId("core_code_search_id").style.display = "";
			}
			// replace school text with subject
			dojo.byId("subject_search_id").innerHTML = "Subject";
			// unhide multiple subjects link
			dojo.byId("multi_subject_link").style.display = "";
			dojo.byId("multi_subject_link_placeholder").style.display = "none";
		}
	};
})();

// SearchUtils
var SearchUtils = (function() {
	return {
		searchBySchool: function() {
			dojo.toggleClass("searchByLocationMessage", "hidden", true);
			dojo.toggleClass("searchBySchoolMessage", "hidden", false);
			dojo.toggleClass("div-location", "hidden", true);
			dojo.toggleClass("div-level", "hidden", true);
			dojo.toggleClass("div-school", "hidden", false);
			Constants.searchType = Constants.SCHOOL_SEARCH;
		},
		searchByLocation: function() {
			dojo.toggleClass("searchByLocationMessage", "hidden", false);
			dojo.toggleClass("searchBySchoolMessage", "hidden", true);
			dojo.toggleClass("div-location", "hidden", false);
			dojo.toggleClass("div-level", "hidden", false);
			dojo.toggleClass("div-school", "hidden", true);
			Constants.searchType = Constants.LOCATION_SEARCH;
		},
		isLocationSearchType: function() {
			return Constants.searchType == Constants.LOCATION_SEARCH;
		},
		isUnitCourseSearchType: function() {
			return Constants.searchType == Constants.UNIT_COURSE_SEARCH;
		},
		isSchoolSearchType: function() {
			return Constants.searchType == Constants.SCHOOL_SEARCH;
		},
		isCoreCodeSearchType: function() {
			return Constants.searchType == Constants.CORE_CODE_SEARCH;
		}
	};
})();

// SectionUtils
var SectionUtils = (function() {
	return {
		// containsRestrictedSection
		containsRestrictedSection: function(courses) {
			var tmp = false;
			dojo.forEach(courses, function(course) {
				dojo.forEach(course.sections, function(section) {
					if (SectionUtils.isRestrictedSection(section)) {
						tmp = true;
						return;
					}
				});
				if (tmp) return;
			});
			return tmp;
		},
		// isRestrictedSection
		isRestrictedSection: function(section) {
			//if (section.campusCode == "CM" || section.campusCode == "OC") return true;
			return false;
		},
		// getOpenSections
		getOpenSections: function(course) {
			var count = 0;
			dojo.forEach(course.sections, function(section) {
				if (section.printed == "Y" && section.openStatus) count++;
			});
			return count;
		},
		// getTotalSections
		getTotalSections: function(course) {
			var count = 0;
			dojo.forEach(course.sections, function(section) {
				if (section.printed == "Y") count++;
			});
			return count;
		},
		// isAllSectionsPrintFlagDisabled
		isAllSectionsPrintFlagDisabled: function(sections) {
			return dojo.every(sections, function(item) { return SectionUtils.isSectionPrintFlagDisabled(item); });
		},
		// isSectionPrintFlagEnabled
		isSectionPrintFlagEnabled: function(section) {
			return section.printed == "Y";
		},
		// isSectionPrintFlagDisabled
		isSectionPrintFlagDisabled: function(section) {
			return section.printed == "N";
		},
		// hasOpenTo
		hasOpenTo: function(section) {
			if (SectionUtils.hasOpenToMajors(section)) return true;
			if (SectionUtils.hasOpenToUnitMajors(section)) return true;
			if (SectionUtils.hasOpenToMinors(section)) return true;
			return false;
		},
		// hasOpenToUnitMajors
		hasOpenToUnitMajors: function(section) {
			return section.unitMajors && section.unitMajors.length > 0;
		},
		// hasOpenToMajors
		hasOpenToMajors: function(section) {
			return section.majors && section.majors.length > 0;
		},
		// hasOpenToMinors
		hasOpenToMinors: function(section) {
			return section.minors && section.minors.length > 0;
		},
		// getOpenTo
		getOpenTo: function(section) {
			var unitMajorsText = "";
			var majorsText = "";
			var minorsText = "";
			if (SectionUtils.hasOpenToUnitMajors(section)) unitMajorsText = SectionUtils.getOpenToUnitMajors(section);
			if (SectionUtils.hasOpenToMajors(section)) majorsText = SectionUtils.getOpenToMajors(section);
			if (SectionUtils.hasOpenToMinors(section)) minorsText = SectionUtils.getOpenToMinors(section);
			// concat all
			var text = "";
			if (unitMajorsText) text += unitMajorsText + ", ";
			if (majorsText) text += majorsText + ", ";
			if (minorsText) text += minorsText + ", ";
			text = text.substr(0, text.length - 2);
			return text;
		},
		// getOpenToUnitMajors
		getOpenToUnitMajors: function(section) {
			var text = "";
			dojo.forEach(section.unitMajors, function(item) {
				var unitName = SchoolUtils.getSchoolName(item.unitCode);
				var majorName = SubjectUtils.getSubjectDescription(item.majorCode);
				if (!unitName && !majorName) {
					text += item.unitCode + "/" + item.majorCode + ", ";
				}
				else {
					if (!unitName) unitName = item.unitCode;
					if (!majorName) majorName = item.majorCode;
					text += item.unitCode + "/" + item.majorCode + " (" + unitName + " / MAJ: " + majorName + "), ";
				}
			});
			text = text.substring(0, text.length - 2);
			return text;
		},
		// getOpenToMajors
		getOpenToMajors: function(section) {
			// separate schools and majors
			var schools = [];
			var majors = [];
			dojo.forEach(section.majors, function(item) {
				if (item.isUnitCode) schools.push(item);
				else if (item.isMajorCode) majors.push(item);
			});
			// sort schools, majors
			schools = schools.sort(function(a, b) { return a.code > b.code; });
			majors = majors.sort(function(a, b) { return a.code > b.code; });
			// concatenate schools before majors
			var text = "";
			dojo.forEach(schools, function(item) { 
				var description = SchoolUtils.getSchoolName(item.code);
				if (description == Constants.NA) {
					text += item.code + ", ";
				}
				else {
					text += item.code + " (" + description + "), ";
				}
			});
			// add MAJ: if majors exist
			if (majors.length) {
				text += "MAJ: ";
				dojo.forEach(majors, function(item) {
					var description = SubjectUtils.getSubjectDescription(item.code);
					if (!description) {
						text += item.code + ", ";
					}
					else {
						text += item.code + " (" + description + "), ";
					}
				});
			}
			// remove trailing comma, space
			text = text.substring(0, text.length - 2);
			return text;
		},
		// getOpenToMinors
		getOpenToMinors: function(section) {
			var text = "MINOR: ";
			dojo.forEach(section.minors, function(item) {
				var description = SubjectUtils.getSubjectDescription(item.code);
				if (!description) description = MinorUtils.getMinorDescription(item.code);
				if (!description) {
					text += item.code + ", ";
				}
				else {
					text += item.code + " (" + description + "), ";
				}
			});
			text = text.substring(0, text.length - 2);
			return text;
		},
		// getCrossListedSections
		getCrossListedSections: function(section) {
			var sections = "";
			dojo.forEach(section.crossListedSections, function(item) {
				sections += item.offeringUnitCode + ":" + item.subjectCode + ":" + item.courseNumber + ":" + item.sectionNumber + ", ";
			});
			sections = sections.substr(0, sections.length - 2);
			return sections;
		},
		// formatSectionNotes
		formatSectionNotes: function(notes) {
			// separate coreqs and prereqs
			var tmp = notes.substr(0, 1) + notes.substr(1).replace(/\*COREQ/g, ".&nbsp;*COREQ").replace(/\*PREREQ/g, ".&nbsp;*PREREQ").replace(/\*PRE-REQ/g, ".&nbsp;*PREREQ");
			// add whitespace between lines that get concatenated
			tmp = tmp.replace(/AND/g, " AND");
			return tmp;
		}
	};
})();

// SemesterUtils
var SemesterUtils = (function() {
	var buildUnitCourseSemesterTitle = function() {
		var term = Constants.selectedSemester.substring(0, 1);
		term = TermUtils.getAsTermName(term);
		var year = Constants.selectedSemester.substring(1);
		// semester: Fall 2011
		var semester = "<span class='semesterTerm'>" + term + "</span> " + " <span class='semesterYear'>" + year + "</span>";
		// campus: NB, NK
		var _campus = dojo.map(Constants.selectedCampus.split(","), function(code) { return "<span id='campus_" + code + "' campusName='" + CampusUtils.getCampusName(code) + "' class='campus'>" + code + "</span>" }).join("<span class='campusSpacer'></span> ");
		// level: Undergraduate, Graduate
		var _level = "<span class='levels'>" + dojo.map(Constants.selectedLevel.split(","), function(code) { return Constants.LEVELS[code]; }).join("/") + "</span>";
		// title html
		var title = semester + "<span class='titleSpacer'></span>" + _campus + "<span class='titleSpacer'></span>" + _level;
		// add span id
		return "<span id='#semesterTitle'>" + title + "</span>";
	};
	var buildLocationSemesterTitle = function() {	
		var term = Constants.selectedSemester.substring(0, 1);
		term = TermUtils.getAsTermName(term);
		var year = Constants.selectedSemester.substring(1);
		// semester: Fall 2011
		var semester = "<span class='semesterTerm'>" + term + "</span> " + " <span class='semesterYear'>" + year + "</span>";
		// campus: NB, NK
		var _campus = dojo.map(Constants.selectedCampus.split(","), function(code) { return "<span id='campus_" + code + "' campusName='" + CampusUtils.getCampusName(code) + "' class='campus'>" + code + "</span>" }).join("<span class='campusSpacer'></span> ");
		// level: Undergraduate, Graduate
		var _level = "<span class='levels'>" + dojo.map(Constants.selectedLevel.split(","), function(code) { return Constants.LEVELS[code]; }).join("/") + "</span>";
		// title html
		var title = semester + "<span class='titleSpacer'></span>" + _campus + "<span class='titleSpacer'></span>" + _level;
		// add span id
		return "<span id='#semesterTitle'>" + title + "</span>";
	};
	var buildSchoolSemesterTitle = function() {
		var term = Constants.selectedSemester.substring(0, 1);
		term = TermUtils.getAsTermName(term);
		var year = Constants.selectedSemester.substring(1);
		// semester: Fall 2011
		var semester = "<span class='semesterTerm'>" + term + "</span> " + " <span class='semesterYear'>" + year + "</span>";
		// school: School of Arts & Sciences (01)
		var school = "<span class='school'>" + SchoolUtils.getSchoolLabel(Constants.selectedSchool) + "</span>";
		// title html
		var title = semester + "<span class='titleSpacer'></span>" + school;
		// add span id
		return "<span id='#semesterTitle'>" + title + "</span>";
	};
	var _getPreviousSemester = function(semester) {
		var term = semester.code.substring(0, 1);
		var year = semester.code.substring(1);
		if (term == 1) return { name:"Winter " + year, code:"0" + year };
		if (term == 0) return { name:"Fall " + (year - 1), code:"9" + (year - 1) };
		if (term == 9) return { name:"Summer " + year, code:"7" + year };
		return { name:"Spring " + year, code:"1" + year };
	};
	return {
		// parseSemester
		parseSemester: function(text) {
			var term = text.substring(0, 1);
			var year = text.substring(1);
			var name = Constants.SEMESTERCODES[term];
			return { name:name + " " + year, code:text };
		},
		// buildSemesterTitle
		buildSemesterTitle: function() {
			if (Constants.searchType == Constants.LOCATION_SEARCH) return buildLocationSemesterTitle();
			else if (Constants.searchType == Constants.SCHOOL_SEARCH) return buildSchoolSemesterTitle();
			else if (Constants.searchType == Constants.UNIT_COURSE_SEARCH) return buildUnitCourseSemesterTitle();
			else if (Constants.searchType == Constants.CORE_CODE_SEARCH) return buildLocationSemesterTitle();
		},
		// updateSemesterTitle
		updateSemesterTitle: function() {
			dojo.byId("semesterTitleSpan").innerHTML = SemesterUtils.buildSemesterTitle();
		},
		// getSemesters
		getSemesters: function() {
			var semesters = [];
			// get current semester
			var current = {};
			dojo.xhrGet({ url:"current_term_date.json", handleAs:"json", sync:true, error:Constants.ERROR, 
				load:function(json) {
					current.code = json.term + "" + json.year;
					current.name = Constants.SEMESTERCODES[json.term] + " " + json.year;
				}
			});
			// add current semester
			semesters.push(current);
			// add previous semesters
			var tmp = current;
			for (var i = 0; i < 8; i++) {
				tmp = _getPreviousSemester(tmp);
				semesters.push(tmp);
			}
			Constants.SEMESTERS = semesters;
			return semesters;
		}
	};
})();

// StringUtils
var StringUtils = (function() {
	return {
		hasLength: function(text) {
			return dojo.trim(text).length > 0;
		}
	};
})();

// SubjectUtils
var SubjectUtils = (function() {
	return {
		// getSubjectTitles
		getSubjectTitles: function(_subjects) {
			var title = "";
			var subjects = _subjects.split(",");
			for (var i = 0; i < subjects.length; i++) {
				if (subjects[i] && dojo.trim(subjects[i])) {
					var _title = SubjectUtils.getSubjectDescription(subjects[i]);
					title += _title + ", ";
				}
			}
			return title.substr(0, title.length - 2);
		},
		// getSubjectLabel
		getSubjectLabel: function(code) {
			var label = "";
			dojo.forEach(Constants.currentSubjects, function(item) {
				if (item.code == code) {
					label = item.label;
					return;
				}
			});
			return label;
		},
		// getSubjectDescription
		getSubjectDescription: function(subjectCode) {
			var description = "";
			var subjects = Constants.currentSubjects;
			for (var i = 0; i < subjects.length; i++) {
				if (subjects[i].code == subjectCode) description = subjects[i].description;
			}
			return description;
		},
		// getSubjectsFromCourses
		getSubjectsFromCourses: function(courses) {
			// subject codes map
			var subjectCodesMap = { };
			// for each course, add subject code:code
			dojo.forEach(courses, function(course) { subjectCodesMap[course.subject] = course.subject; });
			// map contains unique values, now convert to list
			var subjectCodesArray = [];
			for (var subjectCode in subjectCodesMap) {
				subjectCodesArray.push(subjectCode);
			}
			// now sort the array by subjectCode ascending
			subjectCodesArray = subjectCodesArray.sort(function(a, b) { return a > b; });
			// convert the codes array into an object array
			var subjectsArray = [];
			dojo.forEach(subjectCodesArray, function(code) { subjectsArray.push({ code:code, name:SubjectUtils.getSubjectDescription(code) }); });
			// resort by description
			subjectsArray = subjectsArray.sort(function(a, b) { 				
				var result = -1;
				if (a.name > b.name) result = 1;
				return result;
			});
			return subjectsArray;
		}
	};
})();

// TermUtils
var TermUtils = (function() {
	return {
		getAsTermName: function(code) {
			if (code == 0) return "Winter";
			if (code == 1) return "Spring";
			if (code == 7) return "Summer";
			if (code == 9) return "Fall";
			return code;
		}
	};
})();

// UrlParserUtils
var UrlParserUtils = (function() {
	return {
		// parse
		parse: function(fragment, target) {
			var index1 = fragment.indexOf(target + "=");
			if (index1 == -1) return "";
			var tmp = fragment.substring(index1 + target.length + 1);
			var index2 = tmp.indexOf("&");
			if (index2 == -1) return tmp;
			return tmp.substring(0, index2);
		},
		// parseTerm
		parseTerm: function() {
			var url = window.location.href;
			var index = url.indexOf("term=") + "term=".length;
			var index2 = index + 5;
			return url.substring(index, index2);
		}
	};
})();

// Constants
var Constants = (function() { 
	return {
		isIframe: null,
		iframeUrl: null,
		systemMessage: null,
		courses: null,
		coursesFound: null,
		coursesMap: {},
		coursesIntervalMap: {},
		selectedSemester: "",
		selectedCampus: "",
		selectedLevel: "",
		selectedSubject: "",
		selectedSchool: "",
		selectedUnit: "",
		selectedCourseNumber: "",
		getSelectedTerm: function() {
			return Constants.selectedSemester.substring(0, 1);
		},
		getSelectedYear: function() {
			return Constants.selectedSemester.substring(1);
		},
		isOnlineCourses: function() {
			return Constants.selectedCampus == "ONLINE";
		},
		currentSubjects: [],
		currentKeyword: "",
		currentCoreCode: "",
		LOCATION_SEARCH: "LOCATION_SEARCH",
		SCHOOL_SEARCH: " SCHOOL_SEARCH",
		UNIT_COURSE_SEARCH: "UNIT_COURSE_SEARCH",
		CORE_CODE_SEARCH: "CORE_CODE_SEARCH",
		searchType: "LOCATION_SEARCH",
		NA: "N/A",
		MULTIPLE_SUBJECTS_MAX_COUNT: 5,
		COURSES_OUTPUT_ID: "s2out",
		ERROR: function(e) { alert("There was an error with your request.  Please try again or contact soc_support@email.rutgers.edu\n\n-----\nError: " + e); },
		COURSETYPES: { "T":"Traditional", "O":"Online", "H":"Hybrid", "7":"7 Week" },
		LEVELS: { "U":"Undergraduate", "G":"Graduate" },
		LEVELKEYS: ["U", "G", "U,G"],
		MEETINGDAYNAMES: { "M":"Monday", "T":"Tuesday", "W":"Wednesday", "TH":"Thursday", "F":"Friday", "S":"Saturday", "U":"Sunday" },
		MEETINGDAYRANKINGS: { "M":"1", "T":"2", "W":"3", "TH":"4", "F":"5", "S":"6", "U":"7" },
		SEMESTERCODES: { 0:"Winter", 1:"Spring", 7:"Summer", 9:"Fall" },
		SUBCAMPUSES: { "BUS":"Busch", "CAC":"College Avenue", "LIV":"Livingston", "D/C":"Douglass / Cook", "DNB":"Downtown New Brunswick", "NWK":"Newark", "CAM":"Camden" },
		BOOKSTORES: { "NB":58552, "NK":30058, "CM":65132 },
		CORE_CODES: [],
		EXAMCODES: [
			{ code:"A", name:"By Arrangement" },
			{ code:"B", name:"Group Exam" },
			{ code:"C", name:"Class Hour" },
			{ code:"D", name:"Group Exam" },
			{ code:"F", name:"Group Exam" },
			{ code:"G", name:"Group Exam" },
			{ code:"H", name:"Group Exam" },
			{ code:"I", name:"Group Exam" },
			{ code:"J", name:"Group Exam" },
			{ code:"M", name:"Group Exam" },
			{ code:"O", name:"No Exam" },
			{ code:"Q", name:"Group Exam" },
			{ code:"S", name:"Single Day starting 6:00pm or later and Saturday Courses" }
		],
		SCHOOLS: [],
		CAMPUSES: [
			{ code:"NB", name:"New Brunswick" },
			{ code:"NK", name:"Newark" },
			{ code:"CM", name:"Camden" },
			{ code:"B", name:"Burlington County Community College - Mt Laurel" },
			{ code:"WM", name:"Freehold WMHEC - RU-BCC" },
			{ code:"AC", name:"Mays Landing - RU-ACCC" },
			{ code:"MC", name:"Denville - RU-Morris" },
			{ code:"J", name:"McGuire-Dix-Lakehurst RU-JBMDL" },
			{ code:"RV", name:"North Branch - RU-RVCC" },
			{ code:"CC", name:"Camden County College - Blackwood Campus" },
			{ code:"CU", name:"Cumberland County College" },
			{ code:"D", name:"Mercer County Community College" },
			{ code:"H", name:"County College of Morris" },
			{ code:"ONLINE", name:"Rutgers Online Courses" }
		],
		BUILDINGS: [],
		SEMESTERS: [],
		MINORS: [
			{ code: "010", description: "Accounting" },
			{ code: "013", description: "African Middle Eastern & South Asian Languages & Literatures" },
			{ code: "014", description: "Afro-American Studies" },
			{ code: "016", description: "African Area Studies" },
			{ code: "018", description: "Aging" },
			{ code: "019", description: "Agroecology" },
			{ code: "043", description: "Agricultural Engineering Technology" },
			{ code: "050", description: "American Studies" },
			{ code: "060", description: "Ancient Mediterranean Civilizations" },
			{ code: "065", description: "Animation" },
			{ code: "067", description: "Animal Science" },
			{ code: "070", description: "Anthropology" },
			{ code: "071", description: "Evolutionary Anthropology" },
			{ code: "075", description: "Archaeology" },
			{ code: "080", description: "Art" },
			{ code: "081", description: "Art, Visual" },
			{ code: "082", description: "Art History" },
			{ code: "083", description: "Arts" },
			{ code: "086", description: "Journalism - Newark" },
			{ code: "087", description: "Music - Newark" },
			{ code: "088", description: "Theater Arts - Newark" },
			{ code: "089", description: "Video Production - Newark" },
			{ code: "098", description: "Asian Studies" },
			{ code: "100", description: "Astronomy" },
			{ code: "115", description: "Biochemistry" },
			{ code: "119", description: "Biological Sciences" },
			{ code: "120", description: "Biology" },
			{ code: "130", description: "Botany" },
			{ code: "135", description: "Business Administration" },
			{ code: "149", description: "Central & East European Area Studies" },
			{ code: "160", description: "Chemistry" },
			{ code: "163", description: "Childhood Studies" },
			{ code: "165", description: "Chinese" },
			{ code: "175", description: "Cinema Studies" },
			{ code: "184", description: "Classical Studies" },
			{ code: "185", description: "Cognitive Science" },
			{ code: "189", description: "Communication and Media Studies" },
			{ code: "190", description: "Classical Civilization/Classical Humanities" },
			{ code: "192", description: "Communication/Communication Arts" },
			{ code: "195", description: "Comparative Literature" },
			{ code: "196", description: "Companion Animal Science" },
			{ code: "198", description: "Computer Science" },
			{ code: "199", description: "Comparative and Critical Race and Ethnicity" },
			{ code: "200", description: "Creative Writing" },
			{ code: "202", description: "Criminal Justice" },
			{ code: "203", description: "Dance" },
			{ code: "204", description: "Criminology" },
			{ code: "209", description: "Digital Communication, Information and Media" },
			{ code: "215", description: "Ecology and Evolution" },
			{ code: "220", description: "Economics" },
			{ code: "296", description: "Diversity in the Workplace" },
			{ code: "300", description: "Education" },
			{ code: "336", description: "Electronic Arts" },
			{ code: "350", description: "English" },
			{ code: "352", description: "American Literature" },
			{ code: "353", description: "English Literature" },
			{ code: "354", description: "Film" },
			{ code: "355", description: "Business and Technical Writing" },
			{ code: "360", description: "European Studies" },
			{ code: "369", description: "Entomology and Economic Zoology" },
			{ code: "370", description: "Entomology" },
			{ code: "371", description: "Environmental Policy, Institutions and Behavior" },
			{ code: "372", description: "Environmental Resource Monitoring" },
			{ code: "373", description: "Environmental and Business Economics" },
			{ code: "374", description: "Environmental Policy, Institutions and Behavior" },
			{ code: "375", description: "Environmental Sciences" },
			{ code: "378", description: "Exercise Science and Wellness" },
			{ code: "379", description: "Equine Science" },
			{ code: "380", description: "Film Studies" },
			{ code: "382", description: "Entrepeneurship" },
			{ code: "383", description: "Ethics" },
			{ code: "384", description: "Endocrine Physiology & Health Sciences" },
			{ code: "390", description: "Finance" },
			{ code: "400", description: "Food Science" },
			{ code: "420", description: "French" },
			{ code: "425", description: "Geospatial Information Science" },
			{ code: "450", description: "Geography" },
			{ code: "460", description: "Geological Sciences" },
			{ code: "470", description: "German" },
			{ code: "486", description: "Graphic Design" },
			{ code: "489", description: "Modern Greek" },
			{ code: "491", description: "Ancient Greek" },
			{ code: "492", description: "Greek and Latin" },
			{ code: "493", description: "Family/Community Health" },
			{ code: "494", description: "Health Management" },
			{ code: "496", description: "Health Care" },
			{ code: "497", description: "Health Analysis and Research" },
			{ code: "498", description: "Health Issues and Policy" },
			{ code: "500", description: "Hebraic Studies" },
			{ code: "503", description: "Modern Hebrew Language" },
			{ code: "509", description: "History of Women" },
			{ code: "510", description: "General History" },
			{ code: "511", description: "Business History" },
			{ code: "513", description: "History, French" },
			{ code: "516", description: "History of Law" },
			{ code: "517", description: "History of Technology and Science" },
			{ code: "531", description: "Housing, Land Development & Real Estate" },
			{ code: "532", description: "Human Ecology" },
			{ code: "533", description: "Human Resource Management" },
			{ code: "535", description: "Hungarian" },
			{ code: "538", description: "Human Resources" },
			{ code: "551", description: "International Affairs/International Studies" },
			{ code: "555", description: "Individualized Minor" },
			{ code: "557", description: "International Politics" },
			{ code: "560", description: "Italian" },
			{ code: "562", description: "Irish Literature" },
			{ code: "563", description: "Jewish Studies" },
			{ code: "565", description: "Japanese" },
			{ code: "570", description: "Journalism/English-Journalism, Writing and Media" },
			{ code: "572", description: "Justice and Society" },
			{ code: "574", description: "Korean" },
			{ code: "575", description: "Labor Studies" },
			{ code: "579", description: "Lab Unions & Social Mvmts" },
			{ code: "580", description: "Latin" },
			{ code: "583", description: "Language Culture of Ancient Israel" },
			{ code: "590", description: "Latin American Studies" },
			{ code: "595", description: "Latino and Hispanic Caribbean Studies" },
			{ code: "596", description: "Law & the Workplace" },
			{ code: "603", description: "Legal Studies" },
			{ code: "604", description: "Lesbian, Gay, Bisexual, Transgender/Queer Studies" },
			{ code: "607", description: "Leadership Skills" },
			{ code: "615", description: "Linguistics" },
			{ code: "620", description: "General Management" },
			{ code: "623", description: "Management Information Systems" },
			{ code: "628", description: "Marine Sciences" },
			{ code: "629", description: "Marine and Coastal Sciences" },
			{ code: "630", description: "Marketing" },
			{ code: "640", description: "Mathematics" },
			{ code: "657", description: "Media Studies" },
			{ code: "667", description: "Medieval Studies" },
			{ code: "670", description: "Meteorology" },
			{ code: "680", description: "Microbiology" },
			{ code: "683", description: "Middle East Studies" },
			{ code: "685", description: "Middle East Studies" },
			{ code: "690", description: "Museum Studies" },
			{ code: "700", description: "Music" },
			{ code: "701", description: "Musical Theater" },
			{ code: "703", description: "National" },
			{ code: "704", description: "Ecology, Evolution, and Natural Resources" },
			{ code: "707", description: "Nutrition" },
			{ code: "711", description: "Operations Research" },
			{ code: "713", description: "Organizational Leadership" },
			{ code: "714", description: "Organizational Leadership" },
			{ code: "719", description: "Packaging Engineering" },
			{ code: "730", description: "Philosophy" },
			{ code: "733", description: "Philosophy-Applied Ethics" },
			{ code: "750", description: "Physics" },
			{ code: "762", description: "Planning and Public Policy" },
			{ code: "776", description: "Plant Science" },
			{ code: "789", description: "Political Economy" },
			{ code: "790", description: "Political Science" },
			{ code: "810", description: "Portuguese" },
			{ code: "812", description: "Portuguese/Lusophone World Studies" },
			{ code: "825", description: "Professional Outreach and Development" },
			{ code: "827", description: "Public Service" },
			{ code: "830", description: "Psychology" },
			{ code: "831", description: "Public Administration, Executive" },
			{ code: "832", description: "Public Health Policy" },
			{ code: "833", description: "Public Policy" },
			{ code: "834", description: "Public Administration" },
			{ code: "836", description: "Puerto Rican and Hispanic Caribbean Studies" },
			{ code: "840", description: "Religion" },
			{ code: "843", description: "Registered Nurse" },
			{ code: "860", description: "Russian" },
			{ code: "861", description: "Slavic & East European Area Studies" },
			{ code: "879", description: "Science Teacher Education" },
			{ code: "880", description: "Science, Technology and Society" },
			{ code: "881", description: "Science Learning" },
			{ code: "888", description: "Sexualities Studies" },
			{ code: "900", description: "Security Intel & Counter-Terr" },
			{ code: "904", description: "Social Justice" },
			{ code: "910", description: "Social Work" },
			{ code: "920", description: "Sociology" },
			{ code: "925", description: "South Asia Studies" },
			{ code: "930", description: "Soils & Crops" },
			{ code: "940", description: "Spanish" },
			{ code: "958", description: "Television and Speech" },
			{ code: "959", description: "Television" },
			{ code: "960", description: "Statistics" },
			{ code: "964", description: "Theater" },
			{ code: "965", description: "Theater Arts" },
			{ code: "975", description: "Urban Studies" },
			{ code: "976", description: "Urban Planning" },
			{ code: "977", description: "Urban Studies and Community Development" },
			{ code: "981", description: "Voluntary Organization Leadership/Management" },
			{ code: "982", description: "Walt Whitman Studies" },
			{ code: "986", description: "Work Global & Migration" },
			{ code: "987", description: "Work Org & Management" },
			{ code: "988", description: "Women's Studies" },
			{ code: "989", description: "Writing" },
			{ code: "990", description: "Zoology" }
		]
	};
})();
 