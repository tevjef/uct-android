package com.tevinjeffrey.rutgersct.data.rutgersapi;

import com.tevinjeffrey.rutgersct.data.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.data.rutgersapi.model.Subject;
import com.tevinjeffrey.rutgersct.data.rutgersapi.model.SystemMessage;
import java.util.List;
import java.util.Map;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface RetroRutgersService {

  @GET("/subjects.json")
  Observable<List<Subject>> getSubjects(@QueryMap Map<String, String> options);

  @GET("/courses.json")
  Observable<List<Course>> getCourses(@QueryMap Map<String, String> options);

  @GET("/current_system_message.json")
  Observable<SystemMessage> getSystemMessage();

  String SUBJECT_JSON = "[\n" +
      "    {\n" +
      "        \"description\": \"ACADEMIC FOUNDATIONS\",\n" +
      "        \"code\": \"003\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ACCOUNTING\",\n" +
      "        \"code\": \"010\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ADMINISTRATIVE STUDIES\",\n" +
      "        \"code\": \"011\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"AFRICAN STUDIES\",\n" +
      "        \"code\": \"016\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"AFRICAN, M. EAST. & S. ASIAN LANG & LIT\",\n" +
      "        \"code\": \"013\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"AFRO-AMERICAN STUDIES\",\n" +
      "        \"code\": \"014\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"AGRICULTURE AND FOOD SYSTEMS\",\n" +
      "        \"code\": \"020\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ALCOHOL STUDIES\",\n" +
      "        \"code\": \"047\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"AMERICAN LANGUAGE STUDIES\",\n" +
      "        \"code\": \"049\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"AMERICAN STUDIES\",\n" +
      "        \"code\": \"050\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ANIMAL SCIENCE\",\n" +
      "        \"code\": \"067\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ANTHROPOLOGY\",\n" +
      "        \"code\": \"070\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"APPLIED MATHEMATICS\",\n" +
      "        \"code\": \"642\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"APPLIED PHYSICS\",\n" +
      "        \"code\": \"755\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ARABIC LANGUAGES\",\n" +
      "        \"code\": \"074\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ARMENIAN\",\n" +
      "        \"code\": \"078\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ART\",\n" +
      "        \"code\": \"080\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ART HISTORY\",\n" +
      "        \"code\": \"082\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ARTS AND SCIENCES\",\n" +
      "        \"code\": \"090\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ARTS,CULTURE AND MEDIA\",\n" +
      "        \"code\": \"083\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ASIAN STUDIES\",\n" +
      "        \"code\": \"098\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ASTROPHYSICS\",\n" +
      "        \"code\": \"105\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ATMOSPHERIC SCIENCE\",\n" +
      "        \"code\": \"107\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"BEHAVIORAL AND NEURAL SCIENCES\",\n" +
      "        \"code\": \"112\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"BIOCHEMISTRY\",\n" +
      "        \"code\": \"115\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"BIOENVIRONMENTAL ENGINEERING\",\n" +
      "        \"code\": \"117\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"BIOLOGICAL SCIENCES\",\n" +
      "        \"code\": \"119\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"BIOLOGY\",\n" +
      "        \"code\": \"120\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"BIOLOGY: COMPUTATIONAL AND INTEGRATIVE\",\n" +
      "        \"code\": \"121\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"BIOMEDICAL ENGINEERING\",\n" +
      "        \"code\": \"125\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"BIOMEDICAL TECHNOLOGY\",\n" +
      "        \"code\": \"124\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"BIOTECHNOLOGY\",\n" +
      "        \"code\": \"126\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"BUSINESS ADMINISTRATION\",\n" +
      "        \"code\": \"135\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"BUSINESS ANALYTICS AND INFORMATION TECH \",\n" +
      "        \"code\": \"136\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"BUSINESS AND SCIENCE\",\n" +
      "        \"code\": \"137\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"BUSINESS LAW\",\n" +
      "        \"code\": \"140\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"CELL & DEVELOPMENTAL BIOLOGY\",\n" +
      "        \"code\": \"148\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"CELL BIO & NEURO SCI\",\n" +
      "        \"code\": \"146\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"CHEMICAL AND BIOCHEMICAL ENGINEERING\",\n" +
      "        \"code\": \"155\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"CHEMICAL BIOLOGY\",\n" +
      "        \"code\": \"158\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"CHEMISTRY\",\n" +
      "        \"code\": \"160\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"CHILDHOOD STUDIES\",\n" +
      "        \"code\": \"163\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"CHINESE\",\n" +
      "        \"code\": \"165\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"CINEMA STUDIES\",\n" +
      "        \"code\": \"175\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"CIVIL AND ENVIRONMENTAL ENGINEERING\",\n" +
      "        \"code\": \"180\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"CLASSICS\",\n" +
      "        \"code\": \"190\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"CLINICAL PSYCHOLOGY\",\n" +
      "        \"code\": \"821\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"COGNITIVE SCIENCES\",\n" +
      "        \"code\": \"185\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"COLLEGE AND UNIVERSITY LEADERSHIP\",\n" +
      "        \"code\": \"187\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"COLLEGE TEACHING\",\n" +
      "        \"code\": \"186\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"COMMUNICATION\",\n" +
      "        \"code\": \"192\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"COMMUNICATION AND INFORMATION\",\n" +
      "        \"code\": \"189\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"COMMUNICATION AND INFORMATION STUDIES\",\n" +
      "        \"code\": \"194\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"COMMUNITY HEALTH OUTREACH\",\n" +
      "        \"code\": \"193\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"COMPARATIVE LITERATURE\",\n" +
      "        \"code\": \"195\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"COMPUTATIONAL BIO & MOLECULAR BIOPHYSICS\",\n" +
      "        \"code\": \"118\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"COMPUTER SCIENCE\",\n" +
      "        \"code\": \"198\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"CREATIVE WRITING\",\n" +
      "        \"code\": \"200\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"CRIMINAL JUSTICE\",\n" +
      "        \"code\": \"202\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"DANCE\",\n" +
      "        \"code\": \"203\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"DANCE - MGSA\",\n" +
      "        \"code\": \"206\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"DANCE EDUCATION\",\n" +
      "        \"code\": \"207\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"DIGITAL FILMMAKING\",\n" +
      "        \"code\": \"211\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EAST ASIAN LANGUAGE AND CULTURES\",\n" +
      "        \"code\": \"217\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EAST ASIAN LANGUAGES AND AREA STUDIES\",\n" +
      "        \"code\": \"214\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ECOLOGY\",\n" +
      "        \"code\": \"215\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ECOLOGY, EVOLUTION AND NATURAL RESOURCES\",\n" +
      "        \"code\": \"216\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ECONOMICS\",\n" +
      "        \"code\": \"220\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ECONOMICS, APPLIED\",\n" +
      "        \"code\": \"223\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EDUCATION\",\n" +
      "        \"code\": \"300\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EDUCATION-ADULT AND CONTINUING EDUCATION\",\n" +
      "        \"code\": \"233\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EDUCATION-COLLEGE STUDENT AFFAIRS\",\n" +
      "        \"code\": \"245\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EDUCATION-COUNSELING PSYCHOLOGY\",\n" +
      "        \"code\": \"297\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EDUCATION-DESIGN OF LEARNING ENVIRON\",\n" +
      "        \"code\": \"262\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EDUCATION-EARLY CHILD/ELEMENTARY EDUC\",\n" +
      "        \"code\": \"251\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EDUCATION-EDUC STATS & MEASUREMENT\",\n" +
      "        \"code\": \"291\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EDUCATION-EDUCATION ELECTIVES\",\n" +
      "        \"code\": \"255\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EDUCATION-EDUCATION, GENERAL ELECTIVE\",\n" +
      "        \"code\": \"250\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EDUCATION-EDUCATIONAL ADMIN AND SUPERV\",\n" +
      "        \"code\": \"230\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EDUCATION-EDUCATIONAL PSYCHOLOGY\",\n" +
      "        \"code\": \"290\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EDUCATION-ENGLISH/LANG ARTS EDUCATION\",\n" +
      "        \"code\": \"252\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EDUCATION-GIFTED EDUCATION\",\n" +
      "        \"code\": \"294\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EDUCATION-LANGUAGE EDUCATION\",\n" +
      "        \"code\": \"253\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EDUCATION-LEARNING COGNITION & DEV\",\n" +
      "        \"code\": \"295\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EDUCATION-MATHEMATICS EDUCATION\",\n" +
      "        \"code\": \"254\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EDUCATION-READING\",\n" +
      "        \"code\": \"299\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EDUCATION-SCIENCE EDUCATION\",\n" +
      "        \"code\": \"256\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EDUCATION-SOCIAL & PHILOS FOUND OF ED\",\n" +
      "        \"code\": \"310\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EDUCATION-SOCIAL EDUCATION\",\n" +
      "        \"code\": \"257\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EDUCATION-SPECIAL EDUCATION\",\n" +
      "        \"code\": \"293\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EDUCATION-TEACHER LEADERSHIP\",\n" +
      "        \"code\": \"267\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ELECTRICAL AND COMPU.\",\n" +
      "        \"code\": \"332\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ENDOCRINOLOGY AND ANIMAL BIOSCIENCES\",\n" +
      "        \"code\": \"340\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ENGLISH\",\n" +
      "        \"code\": \"350\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ENGLISH - AMERICAN LITERATURE\",\n" +
      "        \"code\": \"352\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ENGLISH - FILM STUDIES\",\n" +
      "        \"code\": \"354\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ENGLISH AS A SECOND LANGUAGE\",\n" +
      "        \"code\": \"356\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ENGLISH: COMP & WRITING\",\n" +
      "        \"code\": \"355\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ENGLISH: CREATIVE WRITING\",\n" +
      "        \"code\": \"351\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ENGLISH: LITERATURE\",\n" +
      "        \"code\": \"358\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ENGLISH: THEORIES AND METHODS\",\n" +
      "        \"code\": \"359\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ENTOMOLOGY\",\n" +
      "        \"code\": \"370\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ENTREPRENEURSHIP\",\n" +
      "        \"code\": \"382\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ENVIRON. POLICY, INSTITUTIONS & BEHAVIOR\",\n" +
      "        \"code\": \"374\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ENVIRONMENTAL AND BIOLOGICAL SCIENCES\",\n" +
      "        \"code\": \"015\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ENVIRONMENTAL AND BUSINESS ECONOMICS\",\n" +
      "        \"code\": \"373\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ENVIRONMENTAL GEOLOGY\",\n" +
      "        \"code\": \"380\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ENVIRONMENTAL PLANNING AND DESIGN\",\n" +
      "        \"code\": \"573\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ENVIRONMENTAL SCIENCES\",\n" +
      "        \"code\": \"375\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EUROPEAN STUDIES\",\n" +
      "        \"code\": \"360\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EXCHANGE\",\n" +
      "        \"code\": \"001\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EXCHANGE REGISTRATION\",\n" +
      "        \"code\": \"376\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"EXERCISE SCIENCE AND SPORT STUDIES\",\n" +
      "        \"code\": \"377\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"FINANCE\",\n" +
      "        \"code\": \"390\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"FOOD AND BUSINESS ECONOMICS\",\n" +
      "        \"code\": \"395\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"FOOD SCIENCE\",\n" +
      "        \"code\": \"400\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"FOREIGN LANGUAGES\",\n" +
      "        \"code\": \"415\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"FRENCH\",\n" +
      "        \"code\": \"420\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"GENERAL ENGINEERING\",\n" +
      "        \"code\": \"440\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"GENETICS\",\n" +
      "        \"code\": \"447\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"GEOGRAPHY\",\n" +
      "        \"code\": \"450\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"GEOLOGICAL SCIENCES\",\n" +
      "        \"code\": \"460\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"GERMAN\",\n" +
      "        \"code\": \"470\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"GLOBAL AFFAIRS\",\n" +
      "        \"code\": \"478\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"GRADUATE - NEWARK\",\n" +
      "        \"code\": \"485\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"GRAPHIC DESIGN\",\n" +
      "        \"code\": \"085\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"GREEK\",\n" +
      "        \"code\": \"490\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"GREEK, MODERN\",\n" +
      "        \"code\": \"489\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"HEALTH ADMINISTRATION\",\n" +
      "        \"code\": \"501\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"HEALTH SCIENCES \",\n" +
      "        \"code\": \"499\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"HINDI\",\n" +
      "        \"code\": \"505\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"HISTORICAL METHODS AND SKILLS\",\n" +
      "        \"code\": \"509\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"HISTORY GENERAL/COMPARATIVE\",\n" +
      "        \"code\": \"506\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"HISTORY, AFR ASIA LATIN AM\",\n" +
      "        \"code\": \"508\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"HISTORY, AMERICAN\",\n" +
      "        \"code\": \"512\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"HISTORY, GENERAL\",\n" +
      "        \"code\": \"510\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"HISTORY, WORLD-AFRICAN, ASIAN & LATIN AM\",\n" +
      "        \"code\": \"516\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"HOME ECONOMICS\",\n" +
      "        \"code\": \"520\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"HONORS PROGRAM\",\n" +
      "        \"code\": \"525\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"HOSPITALITY MANAGEMENT\",\n" +
      "        \"code\": \"537\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"HUMAN RESOURCE MANAGEMENT\",\n" +
      "        \"code\": \"533\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"INDUSTRIAL AND SYSTEMS ENGINEERING\",\n" +
      "        \"code\": \"540\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"INDUSTRIAL RELATIONS AND HUMAN RESOURCES\",\n" +
      "        \"code\": \"545\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"INFORMATION SYSTEMS\",\n" +
      "        \"code\": \"548\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"INFORMATION TECHNOLOGY\",\n" +
      "        \"code\": \"544\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"INFORMATION TECHNOLOGY AND INFORMATICS\",\n" +
      "        \"code\": \"547\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"INTERDISCIPLINARY - SEBS\",\n" +
      "        \"code\": \"554\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"INTERDISCIPLINARY STUDIES - ARTS & SCI\",\n" +
      "        \"code\": \"556\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"INTERFUNCTIONAL\",\n" +
      "        \"code\": \"621\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"INTERNATIONAL BUSINESS AND BUSINESS\",\n" +
      "        \"code\": \"522\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"INTERNATIONAL BUSINESS AND BUSINESS\",\n" +
      "        \"code\": \"553\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ITALIAN\",\n" +
      "        \"code\": \"560\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"JAPANESE\",\n" +
      "        \"code\": \"565\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"JAZZ HISTORY AND RESEARCH\",\n" +
      "        \"code\": \"561\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"JEWISH STUDIES\",\n" +
      "        \"code\": \"563\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"JOURNALISM\",\n" +
      "        \"code\": \"086\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"JOURNALISM\",\n" +
      "        \"code\": \"570\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"JOURNALISM AND MEDIA STUDIES\",\n" +
      "        \"code\": \"567\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"KINESIOLOGY AND APPLIED PHYSIOLOGY\",\n" +
      "        \"code\": \"572\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"KOREAN\",\n" +
      "        \"code\": \"574\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"LABOR STUDIES\",\n" +
      "        \"code\": \"575\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"LABOR STUDIES AND EMPLOYMENT RELATIONS\",\n" +
      "        \"code\": \"578\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"LANDSCAPE ARCHITECTURE\",\n" +
      "        \"code\": \"550\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"LATIN\",\n" +
      "        \"code\": \"580\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"LATIN AMERICAN STUDIES\",\n" +
      "        \"code\": \"590\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"LATINO AND HISPANIC CARIBBEAN STUDIES\",\n" +
      "        \"code\": \"595\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"LAW - CAMDEN\",\n" +
      "        \"code\": \"601\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"LAW - NEWARK\",\n" +
      "        \"code\": \"600\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"LAW PROFESSIONAL SKILLS\",\n" +
      "        \"code\": \"602\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"LEADERSHIP SKILLS\",\n" +
      "        \"code\": \"607\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"LIBERAL STUDIES\",\n" +
      "        \"code\": \"606\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"LIBRARY AND INFORMATION SCIENCE \",\n" +
      "        \"code\": \"610\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"LIBRARY SERVICE - PROF IMPROVEMENT\",\n" +
      "        \"code\": \"611\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"LINGUISTICS\",\n" +
      "        \"code\": \"615\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"LITERATURE AND LANGUAGE\",\n" +
      "        \"code\": \"617\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MANAGEMENT\",\n" +
      "        \"code\": \"620\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MANAGEMENT SCIENCE AND INFO SYSTEMS\",\n" +
      "        \"code\": \"623\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MARINE AND COASTAL SCIENCES\",\n" +
      "        \"code\": \"628\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MARKETING\",\n" +
      "        \"code\": \"630\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MATERIALS SCIENCE AND ENGINEERING\",\n" +
      "        \"code\": \"635\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MATHEMATICAL SCIENCE\",\n" +
      "        \"code\": \"645\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MATHEMATICS\",\n" +
      "        \"code\": \"640\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MECHANICAL AND AEROSPACE ENGINEERING\",\n" +
      "        \"code\": \"650\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MEDIA STUDIES\",\n" +
      "        \"code\": \"657\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MEDICAL TECHNOLOGY\",\n" +
      "        \"code\": \"660\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MEDICINAL CHEMISTRY\",\n" +
      "        \"code\": \"663\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MEDIEVAL STUDIES\",\n" +
      "        \"code\": \"667\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"METEOROLOGY\",\n" +
      "        \"code\": \"670\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MICROBIAL BIOLOGY\",\n" +
      "        \"code\": \"682\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MICROBIOLOGY\",\n" +
      "        \"code\": \"680\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MICROMOLECULAR GENETICS\",\n" +
      "        \"code\": \"681\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MIDDLE EASTERN STUDIES\",\n" +
      "        \"code\": \"685\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MILITARY EDUCATION, AIR FORCE\",\n" +
      "        \"code\": \"690\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MILITARY EDUCATION, ARMY\",\n" +
      "        \"code\": \"691\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MILITARY EDUCATION, NAVY\",\n" +
      "        \"code\": \"692\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MOL BIO & BIOCHEM\",\n" +
      "        \"code\": \"694\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MOLECULAR BIOSCIENCES\",\n" +
      "        \"code\": \"695\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MUSEUM STUDIES\",\n" +
      "        \"code\": \"698\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MUSIC\",\n" +
      "        \"code\": \"087\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MUSIC\",\n" +
      "        \"code\": \"700\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MUSIC - MGSA\",\n" +
      "        \"code\": \"702\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MUSIC, APPLIED (UNITS 07 AND 08)\",\n" +
      "        \"code\": \"701\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"MUSIC, APPLIED (UNITS 07 AND 08)\",\n" +
      "        \"code\": \"703\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"NEUROSCIENCE\",\n" +
      "        \"code\": \"710\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"NURSING\",\n" +
      "        \"code\": \"705\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"NUTRITIONAL SCIENCES\",\n" +
      "        \"code\": \"709\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"OCEANOGRAPHY\",\n" +
      "        \"code\": \"712\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"OPERATIONS MANAGEMENT\",\n" +
      "        \"code\": \"716\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"OPERATIONS RESEARCH\",\n" +
      "        \"code\": \"711\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"ORGANIZATIONAL LEADERSHIP\",\n" +
      "        \"code\": \"713\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PACKAGING ENGINEERING\",\n" +
      "        \"code\": \"731\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PEACE AND CONFLICT STUDIES\",\n" +
      "        \"code\": \"735\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PERCEPTUAL SCIENCE\",\n" +
      "        \"code\": \"714\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PHARMACEUTICAL CHEMISTRY\",\n" +
      "        \"code\": \"715\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PHARMACEUTICS\",\n" +
      "        \"code\": \"721\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PHARMACOLOGY AND TOXICOLOGY\",\n" +
      "        \"code\": \"718\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PHARMACY\",\n" +
      "        \"code\": \"720\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PHARMACY PRACTICE AND ADMINISTRATION\",\n" +
      "        \"code\": \"725\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PHILOSOPHY\",\n" +
      "        \"code\": \"730\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PHYSICAL THERAPY\",\n" +
      "        \"code\": \"742\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PHYSICIAN ASSISTANT\",\n" +
      "        \"code\": \"745\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PHYSICS\",\n" +
      "        \"code\": \"750\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PHYSIOLOGY AND INTEGRATIVE BIOLOGY\",\n" +
      "        \"code\": \"761\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PLANNING AND PUBLIC POLICY\",\n" +
      "        \"code\": \"762\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PLANT BIOLOGY\",\n" +
      "        \"code\": \"765\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PLANT SCIENCE\",\n" +
      "        \"code\": \"776\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"POLISH\",\n" +
      "        \"code\": \"787\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"POLITICAL SCIENCE\",\n" +
      "        \"code\": \"790\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PORTUGUESE\",\n" +
      "        \"code\": \"810\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PORTUGUESE AND LUSOPHONE WORLD STUDIES\",\n" +
      "        \"code\": \"812\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PROFESSIONAL PSYCHOLOGY\",\n" +
      "        \"code\": \"820\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PSYCHOLOGY\",\n" +
      "        \"code\": \"830\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PUBLIC ACCOUNTING\",\n" +
      "        \"code\": \"835\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PUBLIC ADMINISTRATION\",\n" +
      "        \"code\": \"834\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PUBLIC ADMINISTRATION ,EXECUTIVE\",\n" +
      "        \"code\": \"831\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PUBLIC AFFAIRS\",\n" +
      "        \"code\": \"824\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PUBLIC HEALTH\",\n" +
      "        \"code\": \"832\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"PUBLIC POLICY\",\n" +
      "        \"code\": \"833\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"QUANTITATIVE FINANCE\",\n" +
      "        \"code\": \"839\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"RELIGION\",\n" +
      "        \"code\": \"840\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"RHETORIC\",\n" +
      "        \"code\": \"842\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"RUSSIAN\",\n" +
      "        \"code\": \"860\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"SCHOOL PSYCHOLOGY\",\n" +
      "        \"code\": \"826\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"SCIENCE, TECHNOLOGY AND SOCIETY\",\n" +
      "        \"code\": \"880\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"SEBS INTERNSHIP\",\n" +
      "        \"code\": \"902\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"SEXUALITIES STUDIES\",\n" +
      "        \"code\": \"888\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"SOCIAL JUSTICE\",\n" +
      "        \"code\": \"904\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"SOCIAL WORK\",\n" +
      "        \"code\": \"910\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"SOCIOLOGY\",\n" +
      "        \"code\": \"920\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"SPANISH\",\n" +
      "        \"code\": \"940\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"SPEECH\",\n" +
      "        \"code\": \"950\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"STATISTICS\",\n" +
      "        \"code\": \"960\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"STATISTICS, FINANCIAL AND RISK MGMT\",\n" +
      "        \"code\": \"958\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"STUDY ABROAD\",\n" +
      "        \"code\": \"959\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"SUPPLY CHAIN MANAGEMENT\",\n" +
      "        \"code\": \"799\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"TEACHER PREPARATION\",\n" +
      "        \"code\": \"964\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"THEATER\",\n" +
      "        \"code\": \"088\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"THEATER\",\n" +
      "        \"code\": \"965\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"THEATER ARTS - MGSA\",\n" +
      "        \"code\": \"966\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"TOXICOLOGY\",\n" +
      "        \"code\": \"963\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"URBAN PLANNING\",\n" +
      "        \"code\": \"971\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"URBAN PLANNING AND POLICY DEVELOPMENT\",\n" +
      "        \"code\": \"970\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"URBAN STUDIES AND COMMUNITY DEVELOPMENT\",\n" +
      "        \"code\": \"975\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"URBAN SYSTEMS\",\n" +
      "        \"code\": \"977\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"VIDEO PRODUCTION\",\n" +
      "        \"code\": \"089\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"VISUAL ARTS\",\n" +
      "        \"code\": \"081\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"WOMEN'S & GENDER STUDIES\",\n" +
      "        \"code\": \"988\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    },\n" +
      "    {\n" +
      "        \"description\": \"WRITING\",\n" +
      "        \"code\": \"989\",\n" +
      "        \"modifiedDescription\": false\n" +
      "    }\n" +
      "]";
}
