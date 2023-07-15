<h1 align="center"> Infralearning - mounter </h1>

This program is part of the <a ref="https://github.com/victordalosto/infralearning">Infralearning</a> project, an AI program used to evaluate the assets of Road Network.

This <b>mounter</b> uses trained data from the <a href="https://github.com/victordalosto/ai-trainer"> AI-trainer</a> and fits together folders and files to be ready to use by the <a href="https://github.com/victordalosto/infralearning-engine">engine</a> for creating AI models using TensorFlow.
<br/><br/>


<h2> How it works </h2>

The program gets from the <a href="https://github.com/victordalosto/ai-trainer"> AI-trainer</a> database all ids that were trained, then fetchs from another repository all datas corresponding to this id, then put together all datas in a folder structure that will be used by the <a href="https://github.com/victordalosto/infralearning-engine">engine</a> for creating AI models.

