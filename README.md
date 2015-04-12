O objetivo deste projeto é avaliar o desempenho de um cluster de servidores Web, caraterizando algumas medidas de interesse e propondo modificações que melhorem o desempenho do sistema. Apesar do funcionamento de um cluster de servidores Web ser bastante complexo, iremos considerar um modelo simplificado deste sistema. Em particular, estaremos interessados apenas no desempenho do cluster propriamente dito e ignoraremos assim as condições da rede e dos clientes. Além disso, iremos considerar que os recursos compartilhados mais escassos são as CPUs e o sistema de discos do cluster. Desta forma, iremos ignorar o impacto de outros recursos compartilhados, como memória, rede local, etc. Para avaliar o desempenho deste sistema você terá que desenvolver um simulador de eventos discretos que seja capaz de resolver o modelo descrito abaixo. O objetivo por trás deste projeto é aprender a projetar e desenvolver um simulador de eventos discretos e a obter medidas de interesse a partir deste.