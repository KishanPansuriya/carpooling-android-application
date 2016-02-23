/**
 * Brazilian Portuguese translation
 * @author Leandro Carvalho <contato@leandrowebdev.net>
 * @version 2011-07-09
 */
if (elFinder && elFinder.prototype && typeof(elFinder.prototype.i18) == 'object') {
	elFinder.prototype.i18.pt_BR = {
		translator : 'Leandro Carvalho &lt;contato@leandrowebdev.net&gt;',
		language   : 'Português',
		direction  : 'ltr',
		messages   : {

			/********************************** errors **********************************/
			'error'                : 'Erro',
			'errUnknown'           : 'Erro desconhecido.',
			'errUnknownCmd'        : 'Comando desconhecido.',
			'errJqui'              : 'Configuração inválida do JQuery UI. Verifique os componentes selectable, draggable e droppable incluidos.',
			'errNode'              : 'elFinder requer um elemento DOM para ser criado.',
			'errURL'               : 'Configuração inválida do elFinder! Você deve setar a opção da URL.',
			'errAccess'            : 'Acesso negado.',
			'errConnect'           : 'Incapaz de conectar ao backend.',
			'errAbort'             : 'Conexão abortada.',
			'errTimeout'           : 'Connection timeout.',
			'errNotFound'          : 'Backend não encontrado.',
			'errResponse'          : 'Resposta inválida do backend.',
			'errConf'              : 'Configuração inválida do backend.',
			'errJSON'              : 'Módulo PHP JSON não está instalado.',
			'errNoVolumes'         : 'Não existe nenhum volume legível disponivel.',
			'errCmdParams'         : 'Parâmetro inválido para o comando "$1".',
			'errDataNotJSON'       : 'Dados não estão no formato JSON.',
			'errDataEmpty'         : 'Dados vazios.',
			'errCmdReq'            : 'Requisição do Backend requer nome de comando.',
			'errOpen'              : 'Incapaz de abrir "$1".',
			'errNotFolder'         : 'Objeto não é uma pasta.',
			'errNotFile'           : 'Objeto não é um arquivo.',
			'errRead'              : 'Incapaz de ler "$1".',
			'errWrite'             : 'Incapaz de escrever em "$1".',
			'errPerm'              : 'Permissão negada.',
			'errLocked'            : '"$1" está bloqueado e não pode ser renomeado, movido ou removido.',
			'errExists'            : 'O nome do arquivo "$1" já existe neste local.',
			'errInvName'           : 'Nome do arquivo inválido.',
			'errFolderNotFound'    : 'Pasta não encontrada.',
			'errFileNotFound'      : 'Arquivo não encontrado.',
			'errTrgFolderNotFound' : 'Pasta de destino "$1" não encontrada.',
			'errPopup'             : 'Navegador impediu abertura da janela popup, Para abrir o arquivo desabilite está  opção no navegador.',
			'errMkdir'             : 'Incapaz de criar a pasta "$1".',
			'errMkfile'            : 'Inca