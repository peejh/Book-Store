DROP TABLE INVENTORY;
DROP TABLE ORDERITEM;
DROP TABLE ORDERS;
DROP TABLE BOOK;
DROP TABLE CATEGORY;
DROP TABLE ADDRESS;
DROP TABLE STOREUSER;

CREATE TABLE STOREUSER (
	user_id INTEGER NOT NULL,
	email VARCHAR(255) NOT NULL,
	name VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL,
	phone SMALLINT,
	is_owner BOOLEAN NOT NULL,
	CONSTRAINT storeuser_pk PRIMARY KEY (user_id)
);

CREATE TABLE ADDRESS (
	addr_id INTEGER NOT NULL
		GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),
	apt_num SMALLINT,
	street_num SMALLINT NOT NULL,
	street_name VARCHAR(255) NOT NULL,
	city VARCHAR(255) NOT NULL,
	province VARCHAR(255) NOT NULL,
	country VARCHAR(255) NOT NULL,
	postal_code VARCHAR(255),
	user_id INTEGER NOT NULL,
	CONSTRAINT addr_pk PRIMARY KEY (addr_id),
	CONSTRAINT addr_user_fk FOREIGN KEY (user_id)
		REFERENCES STOREUSER (user_id)
		ON DELETE CASCADE
);

CREATE TABLE CATEGORY (
	ctgry_id INTEGER NOT NULL
		GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	name VARCHAR(255) NOT NULL,
	CONSTRAINT ctgry_pk PRIMARY KEY (ctgry_id)
);

CREATE TABLE BOOK (
	book_id INTEGER
		GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	title VARCHAR(1000) NOT NULL,
	author VARCHAR(255) NOT NULL,
	ctgry_id INTEGER NOT NULL DEFAULT 0,
	price DOUBLE NOT NULL,
	description LONG VARCHAR NOT NULL,
	isbn VARCHAR(20) NOT NULL,
	CONSTRAINT book_pk PRIMARY KEY (book_id),
	CONSTRAINT book_ctgry_fk FOREIGN KEY (ctgry_id)
		REFERENCES CATEGORY (ctgry_id)
		ON DELETE NO ACTION
);

CREATE TABLE ORDERS (
	order_id INTEGER NOT NULL,
	user_id INTEGER NOT NULL DEFAULT 0,
	order_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	addr_id INTEGER DEFAULT 0,
	status VARCHAR(255) NOT NULL,
	CONSTRAINT orders_pk PRIMARY KEY (order_id),
	CONSTRAINT orders_user_fk FOREIGN KEY (user_id)
		REFERENCES STOREUSER (user_id)
		ON DELETE NO ACTION,
	CONSTRAINT orders_addr_fk FOREIGN KEY (addr_id)
		REFERENCES ADDRESS (addr_id)
		ON DELETE NO ACTION
);

CREATE TABLE ORDERITEM (
	order_id INTEGER NOT NULL,
	book_id INTEGER NOT NULL,
	quantity INTEGER NOT NULL DEFAULT 1,
	CONSTRAINT orderitem_pk PRIMARY KEY (order_id, book_id),
	CONSTRAINT orderitem_orders_fk FOREIGN KEY (order_id)
		REFERENCES ORDERS (order_id)
		ON DELETE CASCADE,
	CONSTRAINT orderitem_book_fk FOREIGN KEY (book_id)
		REFERENCES BOOK (book_id)
		ON DELETE CASCADE
);

CREATE TABLE INVENTORY (
	book_id INTEGER NOT NULL,
	in_stock INTEGER NOT NULL DEFAULT 0,
	CONSTRAINT inventory_pk PRIMARY KEY (book_id),
	CONSTRAINT inventory_book_fk FOREIGN KEY (book_id)
		REFERENCES BOOK (book_id)
		ON DELETE CASCADE	
);

INSERT INTO STOREUSER (user_id, email, name, password, is_owner) VALUES (
1,
'admin@yorku.ca',
'Neo',
'sha1:64000:18:k30NgYPSA2hIofWNfKXNO4r8l5YDDy7J:7XxS0JwW6AXJL85ovACvjBlo',
TRUE
);

INSERT INTO ADDRESS (apt_num, street_num, street_name, city, province, country, postal_code, user_id)
VALUES (0, 742, 'Evergreen Terrace', 'Springfield', 'Oregon', 'United States', '97475', 1);

INSERT INTO ADDRESS (apt_num, street_num, street_name, city, province, country, postal_code, user_id)
VALUES (0, 4700, 'Keele St.', 'Toronto', 'Ontario', 'Canada', 'M3J1P3', 1);

INSERT INTO CATEGORY (name) VALUES 
('Math'),
('Science'),
('Engineering'),
('Programming'),
('History'),
('Art'),
('Fiction'),
('Non-Fiction'),
('Self-Help');

INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Calculus, Early Transcendentals',
'James Stewart',
1,
189.57,
'Success in your calculus course starts here! James Stewart''s CALCULUS: EARLY TRANSCENDENTALS texts are world-wide best-sellers for a reason: they are clear, accurate, and filled with relevant, real-world examples. With CALCULUS: EARLY TRANSCENDENTALS, Eighth Edition, Stewart conveys not only the utility of calculus to help you develop technical competence, but also gives you an appreciation for the intrinsic beauty of the subject. His patient examples and built-in learning aids will help you build your mathematical confidence and achieve your goals in the course.',
'1285741552'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Fundamentals of Differential Equations and Boundary Value Problems',
'Kent Nagle',
1,
122.39,
'Fundamentals of Differential Equations presents the basic theory of differential equations and offers a variety of modern applications in science and engineering. This flexible text allows instructors to adapt to various course emphases (theory, methodology, applications, and numerical methods) and to use commercially available computer software.',
'0321747747'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Introduction to Linear Algebra',
'Gilbert Strang',
1,
136.75,
'Linear algebra is something all mathematics undergraduates and many other students, in subjects ranging from engineering to economics, have to learn. The fifth edition of this hugely successful textbook retains all the qualities of earlier editions while at the same time seeing numerous minor improvements and major additions. The latter include: â€¢ A new chapter on singular values and singular vectors, including ways to analyze a matrix of data â€¢ A revised chapter on computing in linear algebra, with professional-level algorithms and code that can be downloaded for a variety of languages â€¢ A new section on linear algebra and cryptography â€¢ A new chapter on linear algebra in probability and statistics. A dedicated and active website also offers solutions to exercises as well as new exercises from many different sources (e.g. practice problems, exams, development of textbook examples), plus codes in MATLAB, Julia, and Python.',
'0980232775'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('An Introduction to Information Theory',
'Fazlollah Reza',
1,
69.69,
'Written for an engineering audience, this book has a threefold purpose: (1) to present elements of modern probability theory â€” discrete, continuous, and stochastic; (2) to present elements of information theory with emphasis on its basic roots in probability theory; and (3) to present elements of coding theory. The emphasis throughout the book is on such basic concepts as sets, the probability measure associated with sets, sample space, random variables, information measure, and capacity. These concepts proceed from set theory to probability theory and then to information and coding theories. No formal prerequisites are required other than the usual undergraduate mathematics included in an engineering or science program.',
'0070520526'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Book of Proof',
'Richard Hammack',
1,
39.22,
'This book is an introduction to the language and standard proof methods of mathematics. It is a bridge from the computational courses (such as calculus or differential equations) that students typically encounter in their first year of college to a more abstract outlook. It lays a foundation for more theoretical courses such as topology, analysis and abstract algebra.',
'0989472132'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Probability and Statistics for Computer Scientists',
'Michael Baron',
1,
146.00,
'Probability and Statistics for Computer Scientists, Third Edition helps students understand fundamental concepts of Probability and Statistics, general methods of stochastic modeling, simulation, queuing, and statistical data analysis; make optimal decisions under uncertainty; model and evaluate computer systems; and prepare for advanced probability-based courses. Written in a lively style with simple language and now including R as well as MATLAB, this classroom-tested book can be used for one- or two-semester courses.',
'1138044482'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('The Princeton Companion to Applied Mathematics',
'Nicholas Higham',
1,
134.99,
'This is the most authoritative and accessible single-volume reference book on applied mathematics. Featuring numerous entries by leading experts and organized thematically, it introduces readers to applied mathematics and its uses; explains key concepts; describes important equations, laws, and functions; looks at exciting areas of research; covers modeling and simulation; explores areas of application; and more.',
'0691150397'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Feynman Lectures on Physics',
'Richard Feynman',
2,
286.35,
'"The whole thing was basically an experiment," Richard Feynman said late in his career, looking back on the origins of his lectures. The experiment turned out to be hugely successful, spawning a book that has remained a definitive introduction to physics for decades. Ranging from the most basic principles of Newtonian physics through such formidable theories as general relativity and quantum mechanics, Feynman''s lectures stand as a monument of clear exposition and deep insight. Now, we are reintroducing the printed books to the trade, fully corrected, for the first time ever, and in collaboration with Caltech. Timeless and collectible, the lectures are essential reading, not just for students of physics but for anyone seeking an introduction to the field from the inimitable Feynman.',
'0465023827'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Chemistry, the Central Science',
'Theodore Brown',
2,
195.00,
'Unrivaled problem sets, notable scientific accuracy and currency, and remarkable clarity have made Chemistry: The Central Science the leading general chemistry text for more than a decade. Trusted, innovative, and calibrated, the text increases conceptual understanding and leads to greater student success in general chemistry by building on the expertise of the dynamic author team of leading researchers and award-winning teachers.',
'9780134414232'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Fluid Mechanics',
'Frank White',
2,
106.54,
'The seventh edition of White''s Fluid Mechanics offers students a clear and comprehensive presentation of the material that demonstrates the progression from physical concepts to engineering applications and helps students quickly see the practical importance of fluid mechanics fundamentals. The wide variety of topics gives instructors many options for their course and is a useful resource to students long after graduation.',
'0073529346'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Introduction to Electrodynamics',
'David Griffiths',
2,
134.68,
'For junior/senior-level electricity and magnetism courses. This book is known for its clear, concise, and accessible coverage of standard topics in a logical and pedagogically sound order. The highly polished Fourth Edition features a clear, accessible treatment of the fundamentals of electromagnetic theory, providing a sound platform for the exploration of related applications (ac circuits, antennas, transmission lines, plasmas, optics, etc.). Its lean and focused approach employs numerous new examples and problems.',
'0321856562'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Introduction to Elementary Particles',
'David Griffiths',
2,
104.17,
'In the second, revised edition of a well-established textbook, the author strikes a balance between quantitative rigor and intuitive understanding, using a lively, informal style. The first chapter provides a detailed historical introduction to the subject, while subsequent chapters offer a quantitative presentation of the Standard Model. A simplified introduction to the Feynman rules, based on a "toy" model, helps readers learn the calculational techniques without the complications of spin. It is followed by accessible treatments of quantum electrodynamics, the strong and weak interactions, and gauge theories. New chapters address neutrino oscillations and prospects for physics beyond the Standard Model. The book contains a number of worked examples and many end-of-chapter problems. A complete solution manual is available for instructors.',
'9783527406012'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Introduction to Quantum Mechanics',
'David Griffiths',
2,
88.00,
'Changes and additions to the new edition of this classic textbook include a new chapter on symmetries, new problems and examples, improved explanations, more numerical problems to be worked on a computer, new applications to solid state physics, and consolidated treatment of time-dependent potentials.',
'1107189632'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Mechanics of Materials',
'Russell Hibbeler',
2,
199.00,
'Thorough coverage, a highly visual presentation, and increased problem solving from an author you trust. Mechanics of Materials clearly and thoroughly presents the theory and supports the application of essential mechanics of materials principles. Professor Hibbelerâ€™s concise writing style, countless examples, and stunning four-color photorealistic art program â€” all shaped by the comments and suggestions of hundreds of reviewers â€” help readers visualize and master difficult concepts. The Tenth Edition retains the hallmark features synonymous with the Hibbeler franchise, but has been enhanced with the most current information, a fresh new layout, added problem solving, and increased flexibility in the way topics are covered.',
'0134319656'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('The Principia, Mathematical Principles of Natural Philosophy',
'Isaac Newton',
2,
33.00,
'In his monumental 1687 work, Philosophiae Naturalis Principia Mathematica, known familiarly as the Principia, Isaac Newton laid out in mathematical terms the principles of time, force, and motion that have guided the development of modern physical science. Even after more than three centuries and the revolutions of Einsteinian relativity and quantum mechanics, Newtonian physics continues to account for many of the phenomena of the observed world, and Newtonian celestial dynamics is used to determine the orbits of our space vehicles.',
'0520290887'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Atlas of Human Anatomy',
'Frank Netter',
2,
99.84,
'The gold standard of excellence for 25 years, Frank H. Netter, MD''sAtlas of Human Anatomyoffers unsurpassed depictions of the human body in clear, brilliant detail - all from a clinician''s perspective. With its emphasis on anatomic relationships and clinically relevant views, Dr. Netter''s work provides acoherent, lasting visual vocabulary for understanding anatomy and how it applies to medicine today.',
'1455704180'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Human Biology, Concepts and Current Issues',
'Michael Johnson',
2,
154.00,
'Through his teaching, his textbook, and his online blog, award-winning teacher Michael D. Johnson sparks interest in human biology by connecting basic biology to real-world issues that are relevant to your life.  Using a storytelling approach and extensive online support, Human Biology: Concepts and Current Issues Eighth Edition not only demystifies how the human body works but also drives you to become a better, more discerning consumer of health and science information.  Each chapter opens with Johnsonâ€™s popular â€œCurrent Issuesâ€? essays, and within each chapter, â€œBlogInFocusâ€? references direct readers to his frequently-updated blog for breaking human biology-related news.',
'9780134042435'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Introduction to Space Systems, Design and Synthesis',
'Miguel Aguirre',
3,
103.20,
'The definition of all space systems starts with the establishment of its fundamental parameters: requirements to be fulfilled, overall system and satellite design, analysis and design of the critical elements, developmental approach, cost, and schedule. There are only a few texts covering early design of space systems and none of them has been specifically dedicated to it. Furthermore all existing space engineering books concentrate on analysis. None of them deal with space system synthesis â€“ with the interrelations between all the elements of the space system. Introduction to Space Systems concentrates on understanding the interaction between all the forces, both technical and non-technical, which influence the definition of a space system. This book refers to the entire system: space and ground segments, mission objectives as well as to cost, risk, and mission success probabilities.',
'1461437581'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Rocket Propulsion Elements',
'George Sutton',
3,
119.99,
'The recent upsurge in global government and private spending and in space flight events has resulted in many novel applications of rocket propulsion technology. Rocket Propulsion Elements remains the definitive guide to the field, providing a comprehensive introduction to essential concepts and applications. Led by industry veteran George P. Sutton and by Professor Oscar Biblarz, this book provides interdisciplinary coverage including thermodynamics, aerodynamics, flight performance, propellant chemistry and more.',
'1118753658'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Practical Electronics for Inventors',
'Paul Scherz',
3,
48.52,
'Paul Scherz is a physicist/mechanical engineer who received his B.S. in physics from the University of Wisconsin. His area of interest in physics currently focuses on elementary particle interactions, or high energy physics, and he is working on a new theory on the photon problems with Nikolus Kauer (Ph.D. in high energy physics, Munich, Germany). Paul is an inventor/hobbyist in electronics, an area he grew to appreciate through his experience at the Universityâ€™s Department of Nuclear Engineering and Engineering Physics and the Department of Plasma Physics.',
'0071771336'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('The Art of Electronics',
'Paul Horowitz',
3,
131.05,
'At long last, here is the thoroughly revised and updated, and long-anticipated, third edition of the hugely successful Art of Electronics. Widely accepted as the best single authoritative text on electronic circuit design, it will be an indispensable reference and the gold standard for anyone in the field.',
'9780521809269'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Law for Professional Engineers, Canadian and Global Insights',
'Donald Marston',
3,
99.89,
'Donald L. Marston, P.Eng., J.D. is an experienced lawyer and registered professional engineer. An international arbitrator and mediator, his background includes teaching engineering law at the University of Toronto for more than twenty years.  A Fellow of Engineers Canada, he is also a Fellow of  the American College of Construction Lawyers and the Canadian College of Construction Lawyers. He is also is the Canadian correspondent for the International Construction Law Review.',
'1260135901'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Introduction to Engineering Ethics',
'Mike Martin',
3,
110.59,
'Introduction to Engineering Ethics provides the background for discussion of the basic issues in engineering ethics. Emphasis is given to the moral problems engineers face in the corporate setting. It places those issues within a philosophical framework, and it seeks to exhibit their social importance and intellectual challenge. The primary goal is to stimulate critical and responsible reflection on moral issues surrounding engineering practice and to provide the conceptual tools necessary for pursuing those issues.',
'0072483113'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Optimal Control Systems',
'Subbaram Naidu',
3,
58.29,
'The theory of optimal control systems has grown and flourished since the 1960''s. Many texts, written on varying levels of sophistication, have been published on the subject. Yet even those purportedly designed for beginners in the field are often riddled with complex theorems, and many treatments fail to include topics that are essential to a thorough grounding in the various aspects of and approaches to optimal control.',
'0849308925'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Fundamentals of Structural Engineering',
'Jerome Connor',
3,
180.61,
'This book-presents new methods and tools for the integration and simulation of smart devices.  The design approach described in this book explicitly accounts for integration of Smart Systems components and subsystems as a specific constraint. It includes methodologies and EDA tools to enable multi-disciplinary and multi-scale modeling and design, simulation of multi-domain systems, subsystems and components at all levels of abstraction, system integration and exploration for optimization of functional and non-functional metrics.',
'9783319243290'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Cryptography and Network Security',
'William Stallings',
4,
218.01,
'Stallings'' Cryptography and Network Security, Seventh Edition, introduces the reader to the compelling and evolving field of cryptography and network security. In an age of viruses and hackers, electronic eavesdropping, and electronic fraud on a global scale, security is paramount. The purpose of this book is to provide a practical survey of both the principles and practice of cryptography and network security. In the first part of the book, the basic issues to be addressed by a network security capability are explored by providing a tutorial and survey of cryptography and network security technology. The latter part of the book deals with the practice of network security: practical applications that have been implemented and are in use to provide network security.',
'0134444280'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Introduction to Algorithms',
'Thomas Cormen',
4,
121.28,
'Some books on algorithms are rigorous but incomplete; others cover masses of material but lack rigor. Introduction to Algorithms uniquely combines rigor and comprehensiveness. The book covers a broad range of algorithms in depth, yet makes their design and analysis accessible to all levels of readers. Each chapter is relatively self-contained and can be used as a unit of study. The algorithms are described in English and in a pseudocode designed to be readable by anyone who has done a little programming. The explanations have been kept elementary without sacrificing depth of coverage or mathematical rigor.',
'9788120340077'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('The Hacker Playbook',
'Peter Kim',
4,
25.63,
'Back for the third season, The Hacker Playbook 3 (THP3) takes your offensive game to the pro tier. With a combination of new strategies, attacks, exploits, tips and tricks, you will be able to put yourself in the center of the action toward victory.',
'1494932636'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Kali Linux Cookbook',
'Himanshu Sharma',
4,
49.99,
'With attacks on systems and networks evolving continuously, it has become more important than ever to pentest your environment to ensure advanced-level security. Ethical hackers can help improve the security of networks or systems by performing penetration tests to identify security vulnerabilities. Packed with concise and task-oriented recipes, this book will quickly get you started with Kali Linux (version 2018.4 / 2019), the most advanced and popular pentesting distribution, and take you through its core functionalities.',
'1789952301'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('The Art of Software Testing',
'Glenford Myers',
4,
118.25,
'The hardware and software of computing have changed markedly in the three decades since the first edition of The Art of Software Testing, but this book''s powerful underlying analysis has stood the test of time. Whereas most books on software testing target particular development techniques, languages, or testing methods, The Art of Software Testing, Third Edition provides a brief but powerful and comprehensive presentation of time-proven software testing approaches. If your software development project is mission critical, this book is an investment that will pay for itself with the first bug you find.',
'9780471469124'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Deep Learning with Python',
'Francois Chollet',
4,
35.00,
'Deep Learning with Python introduces the field of deep learning using the Python language and the powerful Keras library. Written by Keras creator and Google AI researcher FranÃ§ois Chollet, this book builds your understanding through intuitive explanations and practical examples.',
'9781617294433'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Refactoring, Improving the Design of Existing Code',
'Martin Fowler',
4,
41.07,
'Your class library works, but could it be better? Refactoring: Improving the Design of Existing Code shows how refactoring can make object-oriented code simpler and easier to maintain. Today refactoring requires considerable design know-how, but once tools become available, all programmers should be able to improve their code using refactoring techniques. Besides an introduction to refactoring, this handbook provides a catalog of dozens of tips for improving code. The best thing about Refactoring is its remarkably clear presentation, along with excellent nuts-and-bolts advice, from object expert Martin Fowler. The author is also an authority on software patterns and UML, and this experience helps make this a better book, one that should be immediately accessible to any intermediate or advanced object-oriented developer. (Just like patterns, each refactoring tip is presented with a simple name, a "motivation," and examples using Java and UML.)',
'0133065261'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Search Engine Optimization',
'Bruce Clay',
4,
39.21,
'In Search Engine Optimization All-in-One For Dummies, 3rd Edition, Bruce Clay, whose search engine consultancy predates Googleâ€”shares everything you need to know about SEO. In minibooks that cover the entire topic, you''ll discover how search engines work, how to apply effective keyword strategies, ways to use SEO to position yourself competitively, the latest on international SEO practices, and more.',
'1118921755'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('The C Programming Language',
'Brian Kernighan',
4,
43.19,
'The authors present the complete guide to ANSI standard C language programming. Written by the developers of C, this new version helps readers keep up with the finalized ANSI standard for C while showing how to take advantage of C''s rich set of operators, economy of expression, improved control flow, and data structures. The 2/E has been completely rewritten with additional examples and problem sets to clarify the implementation of difficult language constructs. For years, C programmers have let K&R guide them to building well-structured and efficient programs. Now this same help is available to those working with ANSI compilers. Includes detailed coverage of the C language plus the official C language reference manual for at-a-glance help with syntax notation, declarations, ANSI changes, scope rules, and the list goes on and on.',
'0131103628'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('The C++ Programming Language',
'Bjarne Stroustrup',
4,
51.19,
'The C++ Programming Language, Fourth Edition, delivers meticulous, richly explained, and integrated coverage of the entire language, its facilities, abstraction mechanisms, standard libraries, and key design techniques. Throughout, Stroustrup presents concise, â€œpure C++11â€? examples, which have been carefully crafted to clarify both usage and program design. To promote deeper understanding, the author provides extensive cross-references, both within the book and to the ISO standard.',
'0321563840'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Learn JavaFX 8, Building User Experience and Interface with Java 8',
'Kishori Sharan',
4,
101.40,
'Learn JavaFX 8 shows you how to start developing rich-client desktop applications using your Java skills and provides comprehensive coverage of JavaFX 8''s features. Each chapter starts with an introduction to the topic at hand, followed by a step-by-step discussion of the topic with small snippets of code. The book contains numerous figures aiding readers in visualizing the GUI that is built at every step in the discussion.',
'1484211431'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('The First World War, A Complete History',
'Martin Gilbert',
5,
19.19,
'It was to be the war to end all wars, and it began at 11:15 on the morning of June 28, 1914, in an outpost of the Austro-Hungarian Empire called Sarajevo. It would end officially almost five years later. Unofficially, it has never ended: the horrors we live with today were born in the First World War.',
'0805076174'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Scipio Africanus, Greater than Nappoleon',
'Linddell Hart',
5,
7.99,
'Scipio Africanus (236183 b.c.) was one of the most exciting and dynamic leaders in history. As commander, he never lost a battle. Yet it is his adversary, Hannibal, who has lived on in public memory.As B.H. Liddell Hart writes,"Scipio''s battles are richer in stratagems and ruses--many still feasible today--than those of any other commander in history." Any military enthusiast or historian will find this to be an absorbing, gripping portrait.',
'0306813637'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('The Origins of the Second World War in Europe',
'PMH Bell',
5,
60.00,
'PMH Bell''s famous book is a comprehensive study of the period and debates surrounding the European origins of the Second World War.  He approaches the subject from three different angles: describing the various explanations that have been offered for the war and the historiographical debates that have arisen from them, analysing the ideological, economic and strategic forces at work in Europe during the 1930s, and tracing the course of events from peace in 1932, via the initial outbreak of hostilities in 1939, through to the climactic German attack on the Soviet Union in 1941 which marked the descent into general conflict.',
'1405840285'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Curiousity, How Science Became Interested in Everything',
'Philip Ball',
5,
26.24,
'With the recent landing of the Mars rover Curiosity, it seems safe to assume that the idea of being curious is alive and well in modern scienceâ€”that itâ€™s not merely encouraged but is seen as an essential component of the scientific mission. Yet there was a time when curiosity was condemned. Neither Pandora nor Eve could resist the dangerous allure of unanswered questions, and all knowledge wasnâ€™t equalâ€”for millennia it was believed that there were some things we should not try to know. In the late sixteenth century this attitude began to change dramatically, and in Curiosity: How Science Became Interested in Everything, Philip Ball investigates how curiosity first became sanctionedâ€”when it changed from a vice to a virtue and how it became permissible to ask any and every question about the world.',
'0226045791'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Western Civilizations, Their History and their Culture',
'Carol Symes',
5,
66.00,
'Joshua Cole is Professor of History at the University of Michigan. Specializing in the social and cultural history of France since the Revolution of 1789, he earned his Ph.D. at University of California at Berkeley. He is the author of The Power of Large Numbers: Population, Politics, and Gender in Nineteenth-Century France (2000). Carol Symes is an Associate Professor of history and Director of Undergraduate Studies in the history department at the University of Illinois, Urbana-Champaign, where she has won the top teaching award in the College of Liberal Arts and Science. Her main areas of study include medieval Europe, especially France and England; cultural history; history of information media and communication technologies; history of theatre. Her first book was A Common Stage: Theater and Public Life in Medieval Arras (2007).',
'0393600981'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Design Basics',
'Stephen Pentak',
6,
56.01,
'Filled with hundreds of stunning examples of successful two-dimensional design, this how-to book explains design theory and gives students the tools needed to create successful designs. DESIGN BASICS presents art fundamentals concepts in full two- to four-page spreads, making the text practical and easy for students to refer to while they work.',
'9781285858227'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('The Complete Book of Drawing Techniques',
'Peter Stanyer',
6,
12.76,
'The Complete Book of Drawing Techniques: A Complete Guide for the Artist. One hugely important aspect of an artist''s skill is a facility with various techniques, with which to express his or her individual style. Being fluent in a range of techniques gives the artist a richer creative vocabulary.',
'0572029160'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Art, Everything You Need to Know about the Greatest Artists and their Work',
'Susie Hodge',
6,
11.99,
'From the influential craftsmen of the high renaissance to the Dutch masters, and from the rococo and neoclassical movements of the 18th century to romanticism, modernism and contemporary art, the lives of the great artists are as varied and multifaceted as the works of creative genius they produced.',
'1623650909'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Great Paintings',
'Hosack Janes',
6,
15.90,
'A sumptuous, visual guided tour of sixty-six of the world''s greatest paintings ranging from works by Zhang Zeduan, a twelfth-century Chinese master, to modern masterpieces by Rothko and Anselm Kiefer. Great Paintings is perfect for anyone interested in learning about the world''s most noteworthy artworks.',
'9780756686758'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Illustrator''s Guide to Pen and Pencil Drawing Techniques',
'Harry Borgman',
6,
15.24,
'This volume provides comprehensive instruction in pencil and ink techniques for beginning illustrators and commercial artists. The author, who has also written "Drawing in Ink" and "The Pen and Pencil Technique Book", thoroughly documents key materials and tools. He demonstrates all graphite stroke techniques and variations, then shows how these techniques can be combined to create effective finished pieces of art. Also included is step-by-step instruction for creating works in charcoal, carbon, wax, and colored pencil. The section on ink drawing is similarly structured, with advice on materials, demonstrations of stroke techniques, and detailed instruction for creating successful finished work. Artists will also learn how to combine ink with such other media as wash and markers. The subject matter is diverse; included are portraits, landscapes, and still lifes.',
'0823025381'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('The Lord of the Rings',
'JRR Tolkien',
7,
15.50,
'The Lord of the Rings tells of the great quest undertaken by Frodo and the Fellowship of the Ring: Gandalf the Wizard; the hobbits Merry, Pippin, and Sam; Gimli the Dwarf; Legolas the Elf; Boromir of Gondor; and a tall, mysterious stranger called Strider.',
'9780544003415'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Hamlet',
'William Shakespeare',
7,
8.99,
'"Hamlet" is a work of great complexity and as such has drawn many different critical interpretations. Hamlet has been seen as a victim of circumstance, as an impractical idealist, as the sufferer of an Oedipus complex, as an opportunist wishing to kill his Uncle not for revenge but to ascend to the throne, as the sufferer of a great melancholy, and as a man blinded by his desire for revenge. The true motivations of Hamlet are complex and enigmatic and have been debated for centuries. Read this classic tragedy and decide for yourself where Hamlet''s true motivations lie and how they influence his ultimate demise.',
'1420922531'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Harry Potter',
'JK Rowling',
7,
8.89,
'Harry Potter has no idea how famous he is. That''s because he''s being raised by his miserable aunt and uncle who are terrified Harry will learn that he''s really a wizard, just as his parents were. But everything changes when Harry is summoned to attend an infamous school for wizards, and he begins to discover some clues about his illustrious birthright. From the surprising way he is greeted by a lovable giant, to the unique curriculum and colorful faculty at his unusual school, Harry finds himself drawn deep inside a mystical world he never knew existed and closer to his own noble destiny.',
'0439708184'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Da Vinci Code',
'Dan Brown',
7,
10.99,
'The Da Vinci Code is a reading experience unlike any other. Simultaneously lightning-paced, intelligent, and intricately layered with remarkable research and detail, Dan Brown''s novel is a thrilling masterpieceâ€”from its opening pages to its stunning conclusion.',
'0307474275'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('All Quiet on the Western Front',
'Erich Maria Remarque',
7,
6.99,
'Paul Baumer enlisted with his classmates in the German army of World War I. Youthful, enthusiastic, they become soldiers. But despite what they have learned, they break into pieces under the first bombardment in the trenches. And as horrible war plods on year after year, Paul holds fast to a single vow: to fight against the principles of hate that meaninglessly pits young men of the same generation but different uniforms against each other--if only he can come out of the war alive',
'9780449213940'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('The Brother Karamazov',
'Fyodor Dostoyevsky',
7,
13.50,
'The Brothers Karamasov is a murder mystery, a courtroom drama, and an exploration of erotic rivalry in a series of triangular love affairs involving the â€œwicked and sentimentalâ€? Fyodor Pavlovich Karamazov and his three sonsâ€•the impulsive and sensual Dmitri; the coldly rational Ivan; and the healthy, red-cheeked young novice Alyosha. Through the gripping events of their story, Dostoevsky portrays the whole of Russian life, is social and spiritual striving, in what was both the golden age and a tragic turning point in Russian culture.',
'0374528373'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Schindler''s List',
'Thomas Keneally',
7,
12.99,
'A stunning novel based on the true story of how German war profiteer and factory director Oskar Schindler came to save more Jews from the gas chambers than any other single person during World War II. In this milestone of Holocaust literature, Thomas Keneally, author of Daughter of Mars, uses the actual testimony of the Schindlerjudenâ€”Schindlerâ€™s Jewsâ€”to brilliantly portray the courage and cunning of a good man in the midst of unspeakable evil.',
'0671880314'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Catch-22',
'Joseph Heller',
7,
11.99,
'Set in Italy during World War II, this is the story of the incomparable, malingering bombardier, Yossarian, a hero who is furious because thousands of people he has never met are trying to kill him. But his real problem is not the enemyâ€”it is his own army, which keeps increasing the number of missions the men must fly to complete their service. Yet if Yossarian makes any attempt to excuse himself from the perilous missions heâ€™s assigned, heâ€™ll be in violation of Catch-22, a hilariously sinister bureaucratic rule: a man is considered insane if he willingly continues to fly dangerous combat missions, but if he makes a formal request to be removed from duty, he is proven sane and therefore ineligible to be relieved.',
'1451626657'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('To Kill a Mockingbird',
'Harper Lee',
7,
11.99,
'The unforgettable novel of a childhood in a sleepy Southern town and the crisis of conscience that rocked it, To Kill A Mockingbird became both an instant bestseller and a critical success when it was first published in 1960. It went on to win the Pulitzer Prize in 1961 and was later made into an Academy Award-winning film, also a classic.',
'0446310786'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn)  VALUES ('Prime Obsession,
Bernhard Riemann and the Greatest Unsolved Problem in Mathematics', 'John Derbyshire', 8, 13.94, 'In
1859, Bernhard Riemann, a little-known thirty-two year old mathematician, made a hypothesis while
presenting a paper to the Berlin Academy titled  â€œOn the Number of Prime Numbers Less Than a Given
Quantity.â€?  Today, after 150 years of careful research and exhaustive study, the Riemann
Hyphothesis remains unsolved, with a one-million-dollar prize earmarked for the first person to
conquer it.', '9780452285255');


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('TechNO-Fix',
'Michael Huesemann',
8,
18.14,
'Nanotechnology! Genetic engineering! Miracle Drugs! We are promised that new technological developments will magically save us from the dire consequences of the 300-year fossil-fueled binge known as modern industrial civilization, without demanding any fundamental changes in our behavior. There is a pervasive belief that technological innovation will enable us to continue our current lifestyle indefinitely and will prevent social, economic and environmental collapse.',
'0865717044'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Cosmos',
'Carl Sagan',
8,
29.99,
'This visually stunning book with over 250 full-color illustrations, many of them never before published, is based on Carl Saganâ€™s thirteen-part television series. Told with Saganâ€™s remarkable ability to make scientific ideas both comprehensible and exciting, Cosmos is about science in its broadest human context, how science and civilization grew up together.',
'0375508325'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('The Elegant Universe',
'Brian Greene',
8,
26.85,
'The Elegant Universe makes some of the most sophisticated concepts ever contemplated accessible and thoroughly entertaining, bringing us closer than ever to understanding how the universe works.',
'0393338101'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('The Master Algorithm, How the Quest for the Ultimate Learning will remake our World',
'Pedro Domingos',
8,
17.99,
'In The Master Algorithm, Pedro Domingos lifts the veil to give us a peek inside the learning machines that power Google, Amazon, and your smartphone. He assembles a blueprint for the future universal learner-the Master Algorithm-and discusses what it will mean for business, science, and society. If data-ism is today''s philosophy, this book is its bible.',
'0465065708'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('The Code Book',
'Simon Singh',
8,
15.99,
'In his first book since the bestselling Fermat''s Enigma, Simon Singh offers the first sweeping history of encryption, tracing its evolution and revealing the dramatic effects codes have had on wars, nations, and individual lives. From Mary, Queen of Scots, trapped by her own code, to the Navajo Code Talkers who helped the Allies win World War II, to the incredible (and incredibly simple) logisitical breakthrough that made Internet commerce secure, The Code Book tells the story of the most powerful intellectual weapon ever known: secrecy.',
'0385495323'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Sapiens, A Brief History of Humankind',
'Yuval Noah Harari',
8,
13.99,
'From a renowned historian comes a groundbreaking narrative of humanityâ€™s creation and evolutionâ€”a #1 international bestsellerâ€”that explores the ways in which biology and history have defined us and enhanced our understanding of what it means to be â€œhuman.â€?',
'0062316095'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Surely You''re Joking, Mr. Feynman',
'Richard Feynman',
8,
7.14,
'Richard Feynman, winner of the Nobel Prize in physics, thrived on outrageous adventures. Here he recounts in his inimitable voice his experience trading ideas on atomic physics with Einstein and Bohr and ideas on gambling with Nick the Greek; cracking the uncrackable safes guarding the most deeply held nuclear secrets; accompanying a ballet on his bongo drums; painting a naked female toreador. In short, here is Feynman''s life in all its eccentricâ€•a combustible mixture of high intelligence, unlimited curiosity, and raging chutzpah.',
'0393316041'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('A Brief History of Time',
'Stephen Hawking',
8,
15.99,
'A landmark volume in science writing by one of the great minds of our time, Stephen Hawkingâ€™s book explores such profound questions as: How did the universe beginâ€”and what made its start possible? Does time always flow forward? Is the universe unendingâ€”or are there boundaries? Are there other dimensions in space? What will happen when it all ends?',
'9780553380163'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('Influence, The Psychology of Persuasion',
'Robert Cialdini',
8,
11.99,
'Influence, the classic book on persuasion, explains the psychology of why people say "yes"â€”and how to apply these understandings. Dr. Robert Cialdini is the seminal expert in the rapidly expanding field of influence and persuasion. His thirty-five years of rigorous, evidence-based research along with a three-year program of study on what moves people to change behavior has resulted in this highly acclaimed book.',
'0061241891'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('The Art of War',
'Sun Tzu',
8,
4.70,
'Art of War is almost certainly the most famous study of strategy ever written and has had an extraordinary influence on the history of warfare. The principles Sun-tzu expounded were utilized brilliantly by such great Asian war leaders as Mao Tse-tung, Giap, and Yamamoto. First translated two hundred years ago by a French missionary, Sun-tzu''s Art of War has been credited with influencing Napoleon, the German General Staff, and even the planning for Desert Storm. Many Japanese companies make this book required reading for their key executives. And increasingly, Western business people and others are turning to the Art of War for inspiration and advice on how to succeed in competitive situations of all kinds.',
'1721195092'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('The 48 Laws of Power',
'Robert Greene',
9,
22.99,
'In the book that People magazine proclaimed â€œbeguilingâ€? and â€œfascinating,â€? Robert Greene and Joost Elffers have distilled three thousand years of the history of power into 48 essential laws by drawing from the philosophies of Machiavelli, Sun Tzu, and Carl Von Clausewitz and also from the lives of figures ranging from Henry Kissinger to P.T. Barnum.',
'0140280197'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('The Art of Reasoning, An Introduction to Logic & Critical Thinking',
'David Kelley',
9,
137.46,
'Students learn logic by practicing itâ€•by working through problems, analyzing existing arguments, and constructing their own arguments in plain language and symbolic notation. The Art of Reasoning not only introduces the principles of critical thinking and logic in a clear, accessible, and logical mannerâ€•thus practicing what it preachesâ€•but it also provides ample opportunity for students to hone their skills and master course content.',
'0393930785'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('How to Win Every Argument, the Use and Abuse of Logic',
'Madsen Pirie',
9,
17.12,
'In the second edition of this witty and infectious book, Madsen Pirie builds upon his guide to using - and indeed abusing - logic in order to win arguments. By including new chapters on how to win arguments in writing, in the pub, with a friend, on Facebook and in 140 characters (on Twitter), Pirie provides the complete guide to triumphing in altercations ranging from the everyday to the downright serious.',
'1472529121'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('How to Think Like Sherlock, Improve your Powers of Observation, Memory and Deduction',
'Daniel Smith',
9,
7.99,
'A fun, interactive guide to boost one''s powers of observation using the techniques of the world''s most famous detectiveâ€”mind palaces, nonverbal tells, lie detection, intuition, concentration, alertness, logic, people watching, and more',
'1843179539'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('How to Succeed at University',
'Thomas Klassen',
9,
19.95,
'Going to university is an exciting time of life that involves many things: learning, meeting new people, making decisions, building relationships, and gaining greater independence. But getting a university education can also be a source of undue stress. What courses should I take? What program should I choose? Will I get a job after graduation? Itâ€™s easy to become discouraged when you donâ€™t see what relationship studying Plato, Shakespeare, or Sartre has to the real world.',
'9780774838986'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('How to Analyze People on Sign, The Five Human Types',
'Elsie Lincoln Benedict',
9,
140.60,
'These excerpts from almost 100 years old book will help improve your skill of reading people. How to Analyze People on Sight was written by Elsie Lincoln Benedict and Ralph Paine Benedict in 1921. Read on and see if their theories are still applicable today.',
'1453756248'); 


INSERT INTO BOOK (title, author, ctgry_id, price, description, isbn) 
VALUES ('How Successful People Think, Changing your Thinking, Change your Life',
'John Maxwell',
9,
12.99,
'A Wall Street Journal bestseller, HOW SUCCESSFUL PEOPLE THINK is the perfect, compact read for today''s fast-paced world. America''s leadership expert John C. Maxwell will teach you how to be more creative and when to question popular thinking. You''ll learn how to capture the big picture while focusing your thinking. You''ll find out how to tap into your creative potential, develop shared ideas, and derive lessons from the past to better understand the future. With these eleven keys to more effective thinking, you''ll clearly see the path to personal success.',
'9781599951683'); 

INSERT INTO INVENTORY (book_id, in_stock) VALUES (1,25), (2,74), (3,5), (4,76), (5,85), (6,86),
(7,86), (8,21), (9,98), (10,75), (11,26), (12,3), (13,99), (14,73), (15,63), (16,36), (17,58),
(18,76), (19,62), (20,58), (21,37), (22,71), (23,78), (24,73), (25,1), (26,18), (27,32), (28,45),
(29,2), (30,96), (31,50), (32,29), (33,5), (34,96), (35,78), (36,69), (37,82), (38,49), (39,33),
(40,92), (41,25), (42,77), (43,32), (44,96), (45,79), (46,39), (47,65), (48,26), (49,25), (50,90),
(51,83), (52,82), (53,15), (54,25), (55,86), (56,24), (57,77), (58,53), (59,43), (60,70), (61,90),
(62,62), (63,7), (64,55), (65,90), (66,29), (67,29), (68,62), (69,74), (70,75), (71,0), (72,72),
(73,92)


CREATE TABLE ORDERCOUNT (
	order_count INTEGER NOT NULL DEFAULT 0
);

INSERT INTO ORDERCOUNT (order_count) VALUES (0);